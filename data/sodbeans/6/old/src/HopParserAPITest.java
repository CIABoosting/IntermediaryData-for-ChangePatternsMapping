/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sonify.vm.hop.parser;

import java.io.File;
import java.net.URL;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andy
 */
public class HopParserAPITest {

    public HopParserAPITest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private void parse(File file) {
        HopParserAPI.parse(file);
        if(!HopParserAPI.isParsed()) {
        }
    }

    private File getFile(String name) {
        File file = new File("");
        file = new File(file.getAbsolutePath() + File.separatorChar + "test" +
                File.separatorChar + "hop" +
                File.separatorChar + name);

        System.out.println(name + " output *****************: " + file.getAbsolutePath());
        return file;
    }

    /**
     * Test of GCD of class Hop.
     */
    @Test
    public void testSimple() {
        parse(getFile("simple.hop"));
    }
}
