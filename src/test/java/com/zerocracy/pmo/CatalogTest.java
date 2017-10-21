/**
 * Copyright (c) 2016-2017 Zerocracy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to read
 * the Software only. Permissions is hereby NOT GRANTED to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.zerocracy.pmo;

import com.zerocracy.Xocument;
import com.zerocracy.jstk.Item;
import com.zerocracy.jstk.Project;
import com.zerocracy.jstk.farm.fake.FkFarm;
import com.zerocracy.jstk.farm.fake.FkProject;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.xembly.Directives;

/**
 * Test case for {@link Catalog}.
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 */
public final class CatalogTest {

    @Test
    public void addsAndFindsProjects() throws Exception {
        final Project project = new FkProject();
        final String pid = "67WE3343P";
        try (final Item item = CatalogTest.item(project)) {
            new Xocument(item.path()).bootstrap("pmo/catalog");
            new Xocument(item.path()).modify(
                new Directives()
                    .xpath("/catalog")
                    .add("project")
                    .attr("id", pid)
                    .add("title").set(pid).up()
                    .add("created")
                    .set(
                        ZonedDateTime.now().format(
                            DateTimeFormatter.ISO_INSTANT
                        )
                    )
                    .up()
                    .add("prefix").set("2017/01/AAAABBBBC/").up()
                    .add("publish").set("false")
            );
        }
        final Catalog catalog = new Catalog(project);
        catalog.link(pid, "github", "yegor256");
        try (final Item item = CatalogTest.item(project)) {
            MatcherAssert.assertThat(
                new Xocument(item.path()).xpath("//project[links/link]/@id"),
                Matchers.not(Matchers.emptyIterable())
            );
        }
    }

    @Test
    public void changesPublishStatus() throws Exception {
        final String pid = "67WE334FF";
        final Catalog catalog = new Catalog(new FkProject()).bootstrap();
        catalog.add(pid, "2017/01/67WE334FF/");
        catalog.publish(pid, true);
        MatcherAssert.assertThat(
            catalog.published(pid),
            Matchers.is(true)
        );
    }

    @Test
    public void addsAndRemovesLinks() throws Exception {
        final String pid = "67WE334GG";
        final Catalog catalog = new Catalog(new FkProject()).bootstrap();
        catalog.add(pid, "2017/05/67WE334GG/");
        MatcherAssert.assertThat(catalog.exists(pid), Matchers.is(true));
        final String rel = "jira";
        final String href = "http://example.com:8080";
        catalog.link(pid, rel, href);
        MatcherAssert.assertThat(
            catalog.hasLink(pid, rel, href),
            Matchers.is(true)
        );
        catalog.unlink(pid, rel, href);
        MatcherAssert.assertThat(
            catalog.hasLink(pid, rel, href),
            Matchers.is(false)
        );
    }

    @Test
    public void setProjectTitle() throws Exception {
        final String pid = "000000000";
        final Pmo pmo = new Pmo(new FkFarm());
        final Catalog catalog = new Catalog(pmo).bootstrap();
        catalog.add(pid, "2017/10/000000000/");
        final String title = "test";
        catalog.title(pid, title);
        try (final Item item = CatalogTest.item(pmo)) {
            MatcherAssert.assertThat(
                new Xocument(item.path())
                    .xpath(
                        String.format(
                            "/catalog/project[@id = '%s']/title/text()",
                            pid
                        )
                    ),
                Matchers.contains(title)
            );
        }
    }

    private static Item item(final Project project) throws IOException {
        return project.acq("catalog.xml");
    }

}
