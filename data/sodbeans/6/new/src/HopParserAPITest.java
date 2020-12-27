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
            fail();
        }
    }

    private File getFile(String name) {
        //temporarily dump in an absolute path to make
        //things easier.
        //TODO: Switch to relative path according to the
        //Test location in NetBeans
        File file = new File("C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler");
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

    /**
     * Test of GCD of class Hop.
     */
    @Test
    public void testClass() {
        parse(getFile("class.hop"));
    }
}