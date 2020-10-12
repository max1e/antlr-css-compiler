// Generated from D:/HAN/ASD-APP/Programmeertalen/PO - Compiler/startcode/src/main/antlr4/nl/han/ica/icss/parser\ICSS.g4 by ANTLR 4.8
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ICSSParser}.
 */
public interface ICSSListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ICSSParser#stylesheet}.
	 * @param ctx the parse tree
	 */
	void enterStylesheet(ICSSParser.StylesheetContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#stylesheet}.
	 * @param ctx the parse tree
	 */
	void exitStylesheet(ICSSParser.StylesheetContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#rulesets}.
	 * @param ctx the parse tree
	 */
	void enterRulesets(ICSSParser.RulesetsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#rulesets}.
	 * @param ctx the parse tree
	 */
	void exitRulesets(ICSSParser.RulesetsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#ruleset}.
	 * @param ctx the parse tree
	 */
	void enterRuleset(ICSSParser.RulesetContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#ruleset}.
	 * @param ctx the parse tree
	 */
	void exitRuleset(ICSSParser.RulesetContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#selector}.
	 * @param ctx the parse tree
	 */
	void enterSelector(ICSSParser.SelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#selector}.
	 * @param ctx the parse tree
	 */
	void exitSelector(ICSSParser.SelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#declarationBlock}.
	 * @param ctx the parse tree
	 */
	void enterDeclarationBlock(ICSSParser.DeclarationBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#declarationBlock}.
	 * @param ctx the parse tree
	 */
	void exitDeclarationBlock(ICSSParser.DeclarationBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(ICSSParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(ICSSParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#property}.
	 * @param ctx the parse tree
	 */
	void enterProperty(ICSSParser.PropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#property}.
	 * @param ctx the parse tree
	 */
	void exitProperty(ICSSParser.PropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(ICSSParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(ICSSParser.ValueContext ctx);
}