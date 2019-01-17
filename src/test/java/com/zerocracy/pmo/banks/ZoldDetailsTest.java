/*
 * Copyright (c) 2016-2019 Zerocracy
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
package com.zerocracy.pmo.banks;

import org.cactoos.matchers.TextHasString;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

/**
 * Test case for {@link ZoldDetails}.
 *
 * @since 1.0
 * @checkstyle JavadocMethodCheck (500 lines)
 */
public final class ZoldDetailsTest {

    @Test
    public void dontTouchValidDetails() {
        final String details = "for pizza";
        MatcherAssert.assertThat(
            new ZoldDetails(details),
            new TextHasString(details)
        );
    }

    @Test
    public void replaceInvalidCharacters() {
        // @checkstyle LineLengthCheck (5 lines)
        MatcherAssert.assertThat(
            new ZoldDetails("@emilianodellacasa: Payment for gh:zold-io/zold#356: Order was finished, quality is \"good\""),
            new TextHasString("@emilianodellacasa: Payment for gh:zold-io/zold-356: Order was finished, quality is -good-")
        );
    }

    @Test
    public void truncateLongDetails() {
        // @checkstyle LineLengthCheck (5 lines)
        MatcherAssert.assertThat(
            new ZoldDetails("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"),
            new TextHasString("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
        );
    }
}
