/*
 * Copyright (c) 2015-2016 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opencypher.tools.tck

class validateCodeStyleTest extends TckTestSupport {

  test("should throw on bad styling") {
    assertIncorrect("match (n) return n", "MATCH (n) RETURN n")
  }

  test("should accept good styling") {
    assertCorrect("MATCH (n) RETURN n")
  }

  test("should request space after colon") {
    assertIncorrect("MATCH (n {name:'test'})", "MATCH (n {name: 'test'})")
  }

  test("should not request space after colon if it's a label") {
    assertCorrect("MATCH (n:Label)")
  }

  test("should not request space after colon if it's a relationship type") {
    assertCorrect("MATCH ()-[:T]-()")
  }

  test("should request space after comma") {
    assertIncorrect("WITH [1,2,3] AS list RETURN list,list", "WITH [1, 2, 3] AS list RETURN list, list")
  }

  test("should accept space after comma when present") {
    assertCorrect("WITH [1, 2, 3] AS list RETURN list, list")
  }

  test("should not request space after comma when line breaks") {
    assertCorrect("""MATCH (a),
                    |(b)
                    |RETURN 1
                    """.stripMargin)
  }

  test("should not allow single quotes in strings") {
    assertIncorrect("WITH \"string\" AS string", "WITH 'string' AS string")
  }

  private def assertCorrect(query: String) = {
    withClue("Query did not adhere to the style rules:\n") {
      validateCodeStyle(query) shouldBe None
    }
  }

  private def assertIncorrect(original: String, prettified: String) = {
    withClue("Query wasn't prettified correctly:\n") {
      validateCodeStyle(original) shouldBe
        Some(s"""A query did not follow style requirements:
                |$original
                |
                |Prettified version:
                |$prettified""".stripMargin)
    }
  }
}
