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
package com.zerocracy.stk.pmo.profile

import com.jcabi.xml.XML
import com.zerocracy.farm.Assume
import com.zerocracy.jstk.Project
import com.zerocracy.jstk.SoftException
import com.zerocracy.jstk.cash.Cash
import com.zerocracy.jstk.cash.CashParsingException
import com.zerocracy.pm.ClaimIn
import com.zerocracy.pmo.People

def exec(Project project, XML xml) {
  new Assume(project, xml).type('Set rate')
  People people = new People(project).bootstrap()
  ClaimIn claim = new ClaimIn(xml)
  String author = claim.author()
  if (!claim.hasParam('rate')) {
    throw new SoftException(
      String.format(
        'Your rate is %s.',
        people.rate(author)
      )
    )
  }
  try {
    Cash rate = new Cash.S(claim.param('rate'))
    people.rate(author, rate)
    claim.reply(
      String.format(
        'Rate of "%s" set to %s.',
        author, rate
      )
    ).postTo(project)
  } catch (CashParsingException ex) {
    throw new SoftException(ex.message)
  }
}