// $ANTLR 3.1.2 C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g 2009-03-31 12:28:43
package org.sonify.vm.hop.parser;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class HopParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "CLASS", "ID", "END", "IF", "INT", "NEWLINE", "WS", "'='", "'+'", "'-'", "'*'", "'('", "')'"
    };
    public static final int CLASS=4;
    public static final int WS=10;
    public static final int T__16=16;
    public static final int T__15=15;
    public static final int NEWLINE=9;
    public static final int T__12=12;
    public static final int T__11=11;
    public static final int T__14=14;
    public static final int T__13=13;
    public static final int INT=8;
    public static final int END=6;
    public static final int ID=5;
    public static final int EOF=-1;
    public static final int IF=7;

    // delegates
    // delegators


        public HopParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public HopParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return HopParser.tokenNames; }
    public String getGrammarFileName() { return "C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g"; }


    public static class start_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "start"
    // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:10:1: start : class_declaration ;
    public final HopParser.start_return start() throws RecognitionException {
        HopParser.start_return retval = new HopParser.start_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        HopParser.class_declaration_return class_declaration1 = null;



        try {
            // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:10:7: ( class_declaration )
            // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:10:9: class_declaration
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_class_declaration_in_start34);
            class_declaration1=class_declaration();

            state._fsp--;

            adaptor.addChild(root_0, class_declaration1.getTree());

            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "start"

    public static class class_declaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "class_declaration"
    // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:12:1: class_declaration : CLASS ID ( method_declaration )+ END ;
    public final HopParser.class_declaration_return class_declaration() throws RecognitionException {
        HopParser.class_declaration_return retval = new HopParser.class_declaration_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token CLASS2=null;
        Token ID3=null;
        Token END5=null;
        HopParser.method_declaration_return method_declaration4 = null;


        Object CLASS2_tree=null;
        Object ID3_tree=null;
        Object END5_tree=null;

        try {
            // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:12:20: ( CLASS ID ( method_declaration )+ END )
            // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:13:2: CLASS ID ( method_declaration )+ END
            {
            root_0 = (Object)adaptor.nil();

            CLASS2=(Token)match(input,CLASS,FOLLOW_CLASS_in_class_declaration45); 
            CLASS2_tree = (Object)adaptor.create(CLASS2);
            adaptor.addChild(root_0, CLASS2_tree);

            ID3=(Token)match(input,ID,FOLLOW_ID_in_class_declaration47); 
            ID3_tree = (Object)adaptor.create(ID3);
            adaptor.addChild(root_0, ID3_tree);

            // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:14:3: ( method_declaration )+
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
            	    // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:14:3: method_declaration
            	    {
            	    pushFollow(FOLLOW_method_declaration_in_class_declaration51);
            	    method_declaration4=method_declaration();

            	    state._fsp--;

            	    adaptor.addChild(root_0, method_declaration4.getTree());

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

            END5=(Token)match(input,END,FOLLOW_END_in_class_declaration55); 
            END5_tree = (Object)adaptor.create(END5);
            adaptor.addChild(root_0, END5_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "class_declaration"

    public static class method_declaration_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "method_declaration"
    // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:18:1: method_declaration : ID ( statement )+ END ;
    public final HopParser.method_declaration_return method_declaration() throws RecognitionException {
        HopParser.method_declaration_return retval = new HopParser.method_declaration_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token ID6=null;
        Token END8=null;
        HopParser.statement_return statement7 = null;


        Object ID6_tree=null;
        Object END8_tree=null;

        try {
            // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:19:2: ( ID ( statement )+ END )
            // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:19:4: ID ( statement )+ END
            {
            root_0 = (Object)adaptor.nil();

            ID6=(Token)match(input,ID,FOLLOW_ID_in_method_declaration67); 
            ID6_tree = (Object)adaptor.create(ID6);
            adaptor.addChild(root_0, ID6_tree);

            // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:20:2: ( statement )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==IF) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:20:2: statement
            	    {
            	    pushFollow(FOLLOW_statement_in_method_declaration70);
            	    statement7=statement();

            	    state._fsp--;

            	    adaptor.addChild(root_0, statement7.getTree());

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

            END8=(Token)match(input,END,FOLLOW_END_in_method_declaration75); 
            END8_tree = (Object)adaptor.create(END8);
            adaptor.addChild(root_0, END8_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "method_declaration"

    public static class statement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "statement"
    // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:24:1: statement : if_statement ;
    public final HopParser.statement_return statement() throws RecognitionException {
        HopParser.statement_return retval = new HopParser.statement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        HopParser.if_statement_return if_statement9 = null;



        try {
            // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:24:10: ( if_statement )
            // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:24:12: if_statement
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_if_statement_in_statement84);
            if_statement9=if_statement();

            state._fsp--;

            adaptor.addChild(root_0, if_statement9.getTree());

            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "statement"

    public static class if_statement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "if_statement"
    // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:27:1: if_statement : IF expression ( statement )+ END ;
    public final HopParser.if_statement_return if_statement() throws RecognitionException {
        HopParser.if_statement_return retval = new HopParser.if_statement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token IF10=null;
        Token END13=null;
        HopParser.expression_return expression11 = null;

        HopParser.statement_return statement12 = null;


        Object IF10_tree=null;
        Object END13_tree=null;

        try {
            // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:27:14: ( IF expression ( statement )+ END )
            // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:27:16: IF expression ( statement )+ END
            {
            root_0 = (Object)adaptor.nil();

            IF10=(Token)match(input,IF,FOLLOW_IF_in_if_statement95); 
            IF10_tree = (Object)adaptor.create(IF10);
            adaptor.addChild(root_0, IF10_tree);

            pushFollow(FOLLOW_expression_in_if_statement97);
            expression11=expression();

            state._fsp--;

            adaptor.addChild(root_0, expression11.getTree());
            // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:28:4: ( statement )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==IF) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:28:4: statement
            	    {
            	    pushFollow(FOLLOW_statement_in_if_statement103);
            	    statement12=statement();

            	    state._fsp--;

            	    adaptor.addChild(root_0, statement12.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);

            END13=(Token)match(input,END,FOLLOW_END_in_if_statement108); 
            END13_tree = (Object)adaptor.create(END13);
            adaptor.addChild(root_0, END13_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "if_statement"

    public static class expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "expression"
    // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:31:1: expression : equality ;
    public final HopParser.expression_return expression() throws RecognitionException {
        HopParser.expression_return retval = new HopParser.expression_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        HopParser.equality_return equality14 = null;



        try {
            // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:31:12: ( equality )
            // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:31:14: equality
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_equality_in_expression117);
            equality14=equality();

            state._fsp--;

            adaptor.addChild(root_0, equality14.getTree());

            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "expression"

    public static class equality_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "equality"
    // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:34:1: equality : add ( '=' add )* ;
    public final HopParser.equality_return equality() throws RecognitionException {
        HopParser.equality_return retval = new HopParser.equality_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token char_literal16=null;
        HopParser.add_return add15 = null;

        HopParser.add_return add17 = null;


        Object char_literal16_tree=null;

        try {
            // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:34:9: ( add ( '=' add )* )
            // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:34:11: add ( '=' add )*
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_add_in_equality127);
            add15=add();

            state._fsp--;

            adaptor.addChild(root_0, add15.getTree());
            // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:34:15: ( '=' add )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==11) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:34:16: '=' add
            	    {
            	    char_literal16=(Token)match(input,11,FOLLOW_11_in_equality130); 
            	    char_literal16_tree = (Object)adaptor.create(char_literal16);
            	    adaptor.addChild(root_0, char_literal16_tree);

            	    pushFollow(FOLLOW_add_in_equality132);
            	    add17=add();

            	    state._fsp--;

            	    adaptor.addChild(root_0, add17.getTree());

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "equality"

    public static class add_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "add"
    // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:37:1: add : multiply ( ( '+' | '-' ) multiply )* ;
    public final HopParser.add_return add() throws RecognitionException {
        HopParser.add_return retval = new HopParser.add_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set19=null;
        HopParser.multiply_return multiply18 = null;

        HopParser.multiply_return multiply20 = null;


        Object set19_tree=null;

        try {
            // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:37:5: ( multiply ( ( '+' | '-' ) multiply )* )
            // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:37:7: multiply ( ( '+' | '-' ) multiply )*
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_multiply_in_add144);
            multiply18=multiply();

            state._fsp--;

            adaptor.addChild(root_0, multiply18.getTree());
            // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:37:16: ( ( '+' | '-' ) multiply )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>=12 && LA5_0<=13)) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:37:17: ( '+' | '-' ) multiply
            	    {
            	    set19=(Token)input.LT(1);
            	    if ( (input.LA(1)>=12 && input.LA(1)<=13) ) {
            	        input.consume();
            	        adaptor.addChild(root_0, (Object)adaptor.create(set19));
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_multiply_in_add155);
            	    multiply20=multiply();

            	    state._fsp--;

            	    adaptor.addChild(root_0, multiply20.getTree());

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "add"

    public static class multiply_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "multiply"
    // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:40:1: multiply : atom ( '*' atom )* ;
    public final HopParser.multiply_return multiply() throws RecognitionException {
        HopParser.multiply_return retval = new HopParser.multiply_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token char_literal22=null;
        HopParser.atom_return atom21 = null;

        HopParser.atom_return atom23 = null;


        Object char_literal22_tree=null;

        try {
            // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:40:9: ( atom ( '*' atom )* )
            // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:40:11: atom ( '*' atom )*
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_atom_in_multiply168);
            atom21=atom();

            state._fsp--;

            adaptor.addChild(root_0, atom21.getTree());
            // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:40:16: ( '*' atom )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==14) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:40:17: '*' atom
            	    {
            	    char_literal22=(Token)match(input,14,FOLLOW_14_in_multiply171); 
            	    char_literal22_tree = (Object)adaptor.create(char_literal22);
            	    adaptor.addChild(root_0, char_literal22_tree);

            	    pushFollow(FOLLOW_atom_in_multiply173);
            	    atom23=atom();

            	    state._fsp--;

            	    adaptor.addChild(root_0, atom23.getTree());

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "multiply"

    public static class atom_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "atom"
    // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:43:1: atom : ( INT | ID | '(' expression ')' );
    public final HopParser.atom_return atom() throws RecognitionException {
        HopParser.atom_return retval = new HopParser.atom_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token INT24=null;
        Token ID25=null;
        Token char_literal26=null;
        Token char_literal28=null;
        HopParser.expression_return expression27 = null;


        Object INT24_tree=null;
        Object ID25_tree=null;
        Object char_literal26_tree=null;
        Object char_literal28_tree=null;

        try {
            // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:43:7: ( INT | ID | '(' expression ')' )
            int alt7=3;
            switch ( input.LA(1) ) {
            case INT:
                {
                alt7=1;
                }
                break;
            case ID:
                {
                alt7=2;
                }
                break;
            case 15:
                {
                alt7=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }

            switch (alt7) {
                case 1 :
                    // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:43:9: INT
                    {
                    root_0 = (Object)adaptor.nil();

                    INT24=(Token)match(input,INT,FOLLOW_INT_in_atom186); 
                    INT24_tree = (Object)adaptor.create(INT24);
                    adaptor.addChild(root_0, INT24_tree);


                    }
                    break;
                case 2 :
                    // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:44:4: ID
                    {
                    root_0 = (Object)adaptor.nil();

                    ID25=(Token)match(input,ID,FOLLOW_ID_in_atom191); 
                    ID25_tree = (Object)adaptor.create(ID25);
                    adaptor.addChild(root_0, ID25_tree);


                    }
                    break;
                case 3 :
                    // C:\\Users\\Andy\\Documents\\Andy\\sodbeans\\sodbeans\\trunk\\Compiler\\src\\org\\sonify\\vm\\hop\\parser\\Hop.g:45:4: '(' expression ')'
                    {
                    root_0 = (Object)adaptor.nil();

                    char_literal26=(Token)match(input,15,FOLLOW_15_in_atom196); 
                    char_literal26_tree = (Object)adaptor.create(char_literal26);
                    adaptor.addChild(root_0, char_literal26_tree);

                    pushFollow(FOLLOW_expression_in_atom198);
                    expression27=expression();

                    state._fsp--;

                    adaptor.addChild(root_0, expression27.getTree());
                    char_literal28=(Token)match(input,16,FOLLOW_16_in_atom200); 
                    char_literal28_tree = (Object)adaptor.create(char_literal28);
                    adaptor.addChild(root_0, char_literal28_tree);


                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "atom"

    // Delegated rules


 

    public static final BitSet FOLLOW_class_declaration_in_start34 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CLASS_in_class_declaration45 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_ID_in_class_declaration47 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_method_declaration_in_class_declaration51 = new BitSet(new long[]{0x0000000000000060L});
    public static final BitSet FOLLOW_END_in_class_declaration55 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_method_declaration67 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_statement_in_method_declaration70 = new BitSet(new long[]{0x00000000000000C0L});
    public static final BitSet FOLLOW_END_in_method_declaration75 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_if_statement_in_statement84 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_if_statement95 = new BitSet(new long[]{0x0000000000008120L});
    public static final BitSet FOLLOW_expression_in_if_statement97 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_statement_in_if_statement103 = new BitSet(new long[]{0x00000000000000C0L});
    public static final BitSet FOLLOW_END_in_if_statement108 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_equality_in_expression117 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_add_in_equality127 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_11_in_equality130 = new BitSet(new long[]{0x0000000000008120L});
    public static final BitSet FOLLOW_add_in_equality132 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_multiply_in_add144 = new BitSet(new long[]{0x0000000000003002L});
    public static final BitSet FOLLOW_set_in_add147 = new BitSet(new long[]{0x0000000000008120L});
    public static final BitSet FOLLOW_multiply_in_add155 = new BitSet(new long[]{0x0000000000003002L});
    public static final BitSet FOLLOW_atom_in_multiply168 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_14_in_multiply171 = new BitSet(new long[]{0x0000000000008120L});
    public static final BitSet FOLLOW_atom_in_multiply173 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_INT_in_atom186 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_atom191 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_atom196 = new BitSet(new long[]{0x0000000000008120L});
    public static final BitSet FOLLOW_expression_in_atom198 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_atom200 = new BitSet(new long[]{0x0000000000000002L});

}