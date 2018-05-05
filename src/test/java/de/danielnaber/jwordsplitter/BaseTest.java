/*
 * Copyright 2012 Daniel Naber (http://www.danielnaber.de)
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
package de.danielnaber.jwordsplitter;

import de.danielnaber.jwordsplitter.tools.FileTools;
import junit.framework.TestCase;

import java.io.*;
import java.util.List;

public abstract class BaseTest extends TestCase {

    protected AbstractWordSplitter splitter;
    protected File tmpLexiconFile;

    protected abstract String getDictionaryFile();

    @Override
    public void setUp() throws IOException {
        try (InputStream is = BaseTest.class.getResourceAsStream(getDictionaryFile())) {
            if (is == null) {
                throw new RuntimeException("Could not load " + getDictionaryFile() + " from classpath");
            }
            final String lexicon = FileTools.loadFile(is, "utf-8");
            tmpLexiconFile = File.createTempFile("jwordsplitter-junit", ".txt");
            try (FileOutputStream fos = new FileOutputStream(tmpLexiconFile);
                 OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8")) {
                osw.write(lexicon);
            }
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        if (tmpLexiconFile != null) {
            tmpLexiconFile.delete();
        }
    }

    protected void expect(String expected, String input) {
        final List<String> result = splitter.splitWord(input);
        assertEquals(expected, result.toString());
    }

    protected void expectSubwords(String expected, String input) {
        final List<String> result = splitter.getSubWords(input);
        assertEquals(expected, result.toString());
    }

}
