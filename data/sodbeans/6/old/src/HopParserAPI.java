/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sonify.vm.hop.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.antlr.runtime.*;
import org.openide.util.Exceptions;

/**
 *
 * @author Andreas Stefik
 */
public class HopParserAPI {
    public static void parse(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            ANTLRInputStream input = new ANTLRInputStream(fis);
            HopLexer lexer = new HopLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            HopParser parser = new HopParser(tokens);
            parser.prog();
        } catch (RecognitionException ex) {
            Exceptions.printStackTrace(ex);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
    }

    public static boolean isParsed() {
        return true;
    }
}
