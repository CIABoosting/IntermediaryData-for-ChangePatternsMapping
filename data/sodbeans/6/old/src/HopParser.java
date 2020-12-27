package org.sonify.vm.hop.parser;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class HopParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "NEWLINE", "ID", "INT", "WS", "'='", "'+'", "'-'", "'*'", "'('", "')'"
    };
    public static final int WS=7;
    public static final int NEWLINE=4;
    public static final int T__12=12;
    public static final int T__11=11;
    public static final int T__13=13;
    public static final int T__10=10;
    public static final int INT=6;
    public static final int ID=5;
    public static final int EOF=-1;
    public static final int T__9=9;
    public static final int T__8=8;

        public HopParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public HopParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return HopParser.tokenNames; }
    public String getGrammarFileName() { return "C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g"; }



    public final void prog() throws RecognitionException {
        try {
            {
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>=NEWLINE && LA1_0<=INT)||LA1_0==12) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    {
            	    pushFollow(FOLLOW_stat_in_prog24);
            	    stat();

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
    public final void stat() throws RecognitionException {
        try {
            int alt2=3;
            switch ( input.LA(1) ) {
            case INT:
            case 12:
                {
                alt2=1;
                }
                break;
            case ID:
                {
                int LA2_2 = input.LA(2);

                if ( (LA2_2==8) ) {
                    alt2=2;
                }
                else if ( (LA2_2==NEWLINE||(LA2_2>=9 && LA2_2<=11)) ) {
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
                    pushFollow(FOLLOW_expr_in_stat33);
                    expr();

                    state._fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_stat35); 

                    }
                    break;
                case 2 :
                    {
                    match(input,ID,FOLLOW_ID_in_stat40); 
                    match(input,8,FOLLOW_8_in_stat42); 
                    pushFollow(FOLLOW_expr_in_stat44);
                    expr();

                    state._fsp--;

                    match(input,NEWLINE,FOLLOW_NEWLINE_in_stat46); 

                    }
                    break;
                case 3 :
                    {
                    match(input,NEWLINE,FOLLOW_NEWLINE_in_stat51); 

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
    public final void expr() throws RecognitionException {
        try {
            {
            pushFollow(FOLLOW_multExpr_in_expr60);
            multExpr();

            state._fsp--;

            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>=9 && LA3_0<=10)) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    {
            	    if ( (input.LA(1)>=9 && input.LA(1)<=10) ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_multExpr_in_expr71);
            	    multExpr();

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
    public final void multExpr() throws RecognitionException {
        try {
            {
            pushFollow(FOLLOW_atom_in_multExpr81);
            atom();

            state._fsp--;

            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==11) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    {
            	    match(input,11,FOLLOW_11_in_multExpr84); 
            	    pushFollow(FOLLOW_atom_in_multExpr86);
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
            case 12:
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
                    match(input,INT,FOLLOW_INT_in_atom99); 

                    }
                    break;
                case 2 :
                    {
                    match(input,ID,FOLLOW_ID_in_atom104); 

                    }
                    break;
                case 3 :
                    {
                    match(input,12,FOLLOW_12_in_atom109); 
                    pushFollow(FOLLOW_expr_in_atom111);
                    expr();

                    state._fsp--;

                    match(input,13,FOLLOW_13_in_atom113); 

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
    public static final BitSet FOLLOW_stat_in_prog24 = new BitSet(new long[]{0x0000000000001072L});
    public static final BitSet FOLLOW_expr_in_stat33 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_stat35 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_stat40 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_8_in_stat42 = new BitSet(new long[]{0x0000000000001060L});
    public static final BitSet FOLLOW_expr_in_stat44 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_NEWLINE_in_stat46 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEWLINE_in_stat51 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_multExpr_in_expr60 = new BitSet(new long[]{0x0000000000000602L});
    public static final BitSet FOLLOW_set_in_expr63 = new BitSet(new long[]{0x0000000000001060L});
    public static final BitSet FOLLOW_multExpr_in_expr71 = new BitSet(new long[]{0x0000000000000602L});
    public static final BitSet FOLLOW_atom_in_multExpr81 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_11_in_multExpr84 = new BitSet(new long[]{0x0000000000001060L});
    public static final BitSet FOLLOW_atom_in_multExpr86 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_INT_in_atom99 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_atom104 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_atom109 = new BitSet(new long[]{0x0000000000001060L});
    public static final BitSet FOLLOW_expr_in_atom111 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_atom113 = new BitSet(new long[]{0x0000000000000002L});

}
