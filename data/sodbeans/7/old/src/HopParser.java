package org.sonify.vm.hop.parser;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class HopParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "CLASS", "ID", "END", "NEWLINE", "INT", "WS", "'='", "'+'", "'-'", "'*'", "'('", "')'"
    };
    public static final int CLASS=4;
    public static final int WS=9;
    public static final int T__15=15;
    public static final int NEWLINE=7;
    public static final int T__12=12;
    public static final int T__11=11;
    public static final int T__14=14;
    public static final int T__13=13;
    public static final int T__10=10;
    public static final int INT=8;
    public static final int END=6;
    public static final int ID=5;
    public static final int EOF=-1;

        public HopParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public HopParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return HopParser.tokenNames; }
    public String getGrammarFileName() { return "C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g"; }



    public final void start() throws RecognitionException {
        try {
            {
            pushFollow(FOLLOW_class_declaration_in_start24);
            class_declaration();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    public final void class_declaration() throws RecognitionException {
        try {
            {
            match(input,CLASS,FOLLOW_CLASS_in_class_declaration35); 
            match(input,ID,FOLLOW_ID_in_class_declaration37); 
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==ID) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    {
            	    pushFollow(FOLLOW_method_declaration_in_class_declaration41);
            	    method_declaration();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);

            match(input,END,FOLLOW_END_in_class_declaration45); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    public final void method_declaration() throws RecognitionException {
        try {
            {
            match(input,ID,FOLLOW_ID_in_method_declaration57); 
            match(input,NEWLINE,FOLLOW_NEWLINE_in_method_declaration59); 
            match(input,END,FOLLOW_END_in_method_declaration63); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    public final void statement() throws RecognitionException {
        try {
            int alt2=3;
            switch ( input.LA(1) ) {
            case INT:
            case 14:
                {
                alt2=1;
                }
                break;
            case ID:
                {
                int LA2_2 = input.LA(2);

                if ( (LA2_2==10) ) {
                    alt2=2;
                }
                else if ( (LA2_2==NEWLINE||(LA2_2>=11 && LA2_2<=13)) ) {
                    alt2=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 2, 2, input);

                    throw nvae;
                }
                }
                break;
            case NEWLINE:
                {
                alt2=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    {
                    pushFollow(FOLLOW_expression_in_statement73);
                    expression();

                    state._fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_statement75); 

                    }
                    break;
                case 2 :
                    {
                    match(input,ID,FOLLOW_ID_in_statement80); 
                    match(input,10,FOLLOW_10_in_statement82); 
                    pushFollow(FOLLOW_expression_in_statement84);
                    expression();

                    state._fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_statement86); 

                    }
                    break;
                case 3 :
                    {
                    match(input,NEWLINE,FOLLOW_NEWLINE_in_statement91); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    public final void expression() throws RecognitionException {
        try {
            {
            pushFollow(FOLLOW_multiply_in_expression100);
            multiply();

            state._fsp--;

            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>=11 && LA3_0<=12)) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    {
            	    if ( (input.LA(1)>=11 && input.LA(1)<=12) ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_multiply_in_expression111);
            	    multiply();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    public final void multiply() throws RecognitionException {
        try {
            {
            pushFollow(FOLLOW_atom_in_multiply121);
            atom();

            state._fsp--;

            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==13) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    {
            	    match(input,13,FOLLOW_13_in_multiply124); 
            	    pushFollow(FOLLOW_atom_in_multiply126);
            	    atom();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    public final void atom() throws RecognitionException {
        try {
            int alt5=3;
            switch ( input.LA(1) ) {
            case INT:
                {
                alt5=1;
                }
                break;
            case ID:
                {
                alt5=2;
                }
                break;
            case 14:
                {
                alt5=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }

            switch (alt5) {
                case 1 :
                    {
                    match(input,INT,FOLLOW_INT_in_atom139); 

                    }
                    break;
                case 2 :
                    {
                    match(input,ID,FOLLOW_ID_in_atom144); 

                    }
                    break;
                case 3 :
                    {
                    match(input,14,FOLLOW_14_in_atom149); 
                    pushFollow(FOLLOW_expression_in_atom151);
                    expression();

                    state._fsp--;

                    match(input,15,FOLLOW_15_in_atom153); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    public static final BitSet FOLLOW_class_declaration_in_start24 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CLASS_in_class_declaration35 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_ID_in_class_declaration37 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_method_declaration_in_class_declaration41 = new BitSet(new long[]{0x0000000000000060L});
    public static final BitSet FOLLOW_END_in_class_declaration45 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_method_declaration57 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NEWLINE_in_method_declaration59 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_END_in_method_declaration63 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_statement73 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NEWLINE_in_statement75 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_statement80 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_10_in_statement82 = new BitSet(new long[]{0x0000000000004120L});
    public static final BitSet FOLLOW_expression_in_statement84 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_NEWLINE_in_statement86 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEWLINE_in_statement91 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_multiply_in_expression100 = new BitSet(new long[]{0x0000000000001802L});
    public static final BitSet FOLLOW_set_in_expression103 = new BitSet(new long[]{0x0000000000004120L});
    public static final BitSet FOLLOW_multiply_in_expression111 = new BitSet(new long[]{0x0000000000001802L});
    public static final BitSet FOLLOW_atom_in_multiply121 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_13_in_multiply124 = new BitSet(new long[]{0x0000000000004120L});
    public static final BitSet FOLLOW_atom_in_multiply126 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_INT_in_atom139 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_atom144 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_atom149 = new BitSet(new long[]{0x0000000000004120L});
    public static final BitSet FOLLOW_expression_in_atom151 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_15_in_atom153 = new BitSet(new long[]{0x0000000000000002L});

}
