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
    private static boolean parsed = false;

    public static void parse(File file) {
        
        try {
            FileInputStream fis = new FileInputStream(file);
            ANTLRInputStream input = new ANTLRInputStream(fis);
            HopLexer lexer = new HopLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            HopParser parser = new HopParser(tokens);
            parser.start();
            int i = parser.getNumberOfSyntaxErrors();
            if(i > 0) {
                parsed = false;
            }
            else {
                parsed = true;
            }
        } catch (RecognitionException ex) {
            Exceptions.printStackTrace(ex);
            parsed = false;
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
            parsed = false;
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            parsed = false;
        }
        
    }

    public static boolean isParsed() {
        return parsed;
    }
}
