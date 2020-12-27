package org.sonify.vm.hop.parser;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class HopLexer extends Lexer {
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
    public static final int ID=5;
    public static final int END=6;
    public static final int EOF=-1;

    public HopLexer() {;} 
    public HopLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public HopLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g"; }

    public final void mT__10() throws RecognitionException {
        try {
            int _type = T__10;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    public final void mT__11() throws RecognitionException {
        try {
            int _type = T__11;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    public final void mT__12() throws RecognitionException {
        try {
            int _type = T__12;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    public final void mEND() throws RecognitionException {
        try {
            int _type = END;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
            match("end"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    public final void mCLASS() throws RecognitionException {
        try {
            int _type = CLASS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
            match("class"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>='A' && LA1_0<='Z')||(LA1_0>='a' && LA1_0<='z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    {
            	    if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


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

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='0' && LA2_0<='9')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    public final void mNEWLINE() throws RecognitionException {
        try {
            int _type = NEWLINE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='\r') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    {
                    match('\r'); 

                    }
                    break;

            }

            match('\n'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            {
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='\t' && LA4_0<='\n')||LA4_0=='\r'||LA4_0==' ') ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);

            skip();

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    public void mTokens() throws RecognitionException {
        int alt5=12;
        alt5 = dfa5.predict(input);
        switch (alt5) {
            case 1 :
                {
                mT__10(); 

                }
                break;
            case 2 :
                {
                mT__11(); 

                }
                break;
            case 3 :
                {
                mT__12(); 

                }
                break;
            case 4 :
                {
                mT__13(); 

                }
                break;
            case 5 :
                {
                mT__14(); 

                }
                break;
            case 6 :
                {
                mT__15(); 

                }
                break;
            case 7 :
                {
                mEND(); 

                }
                break;
            case 8 :
                {
                mCLASS(); 

                }
                break;
            case 9 :
                {
                mID(); 

                }
                break;
            case 10 :
                {
                mINT(); 

                }
                break;
            case 11 :
                {
                mNEWLINE(); 

                }
                break;
            case 12 :
                {
                mWS(); 

                }
                break;

        }

    }


    protected DFA5 dfa5 = new DFA5(this);
    static final String DFA5_eotS =
        "\7\uffff\2\11\2\uffff\1\15\1\20\1\uffff\2\11\1\uffff\1\23\1\11"+
        "\1\uffff\1\11\1\26\1\uffff";
    static final String DFA5_eofS =
        "\27\uffff";
    static final String DFA5_minS =
        "\1\11\6\uffff\1\156\1\154\2\uffff\1\12\1\11\1\uffff\1\144\1\141"+
        "\1\uffff\1\101\1\163\1\uffff\1\163\1\101\1\uffff";
    static final String DFA5_maxS =
        "\1\172\6\uffff\1\156\1\154\2\uffff\1\12\1\40\1\uffff\1\144\1\141"+
        "\1\uffff\1\172\1\163\1\uffff\1\163\1\172\1\uffff";
    static final String DFA5_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\2\uffff\1\11\1\12\2\uffff\1\14"+
        "\2\uffff\1\13\2\uffff\1\7\2\uffff\1\10";
    static final String DFA5_specialS =
        "\27\uffff}>";
    static final String[] DFA5_transitionS = {
            "\1\15\1\14\2\uffff\1\13\22\uffff\1\15\7\uffff\1\5\1\6\1\4\1"+
            "\2\1\uffff\1\3\2\uffff\12\12\3\uffff\1\1\3\uffff\32\11\6\uffff"+
            "\2\11\1\10\1\11\1\7\25\11",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\16",
            "\1\17",
            "",
            "",
            "\1\14",
            "\2\15\2\uffff\1\15\22\uffff\1\15",
            "",
            "\1\21",
            "\1\22",
            "",
            "\32\11\6\uffff\32\11",
            "\1\24",
            "",
            "\1\25",
            "\32\11\6\uffff\32\11",
            ""
    };

    static final short[] DFA5_eot = DFA.unpackEncodedString(DFA5_eotS);
    static final short[] DFA5_eof = DFA.unpackEncodedString(DFA5_eofS);
    static final char[] DFA5_min = DFA.unpackEncodedStringToUnsignedChars(DFA5_minS);
    static final char[] DFA5_max = DFA.unpackEncodedStringToUnsignedChars(DFA5_maxS);
    static final short[] DFA5_accept = DFA.unpackEncodedString(DFA5_acceptS);
    static final short[] DFA5_special = DFA.unpackEncodedString(DFA5_specialS);
    static final short[][] DFA5_transition;

    static {
        int numStates = DFA5_transitionS.length;
        DFA5_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA5_transition[i] = DFA.unpackEncodedString(DFA5_transitionS[i]);
        }
    }

    class DFA5 extends DFA {

        public DFA5(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 5;
            this.eot = DFA5_eot;
            this.eof = DFA5_eof;
            this.min = DFA5_min;
            this.max = DFA5_max;
            this.accept = DFA5_accept;
            this.special = DFA5_special;
            this.transition = DFA5_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__10 | T__11 | T__12 | T__13 | T__14 | T__15 | END | CLASS | ID | INT | NEWLINE | WS );";
        }
    }
 

}
