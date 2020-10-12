package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.HashMap;
import java.util.LinkedList;


public class Checker {

    private LinkedList<HashMap<String, ExpressionType>> variableTypes;

    public void check(AST ast) {
        variableTypes = new LinkedList<>();
//        checkStylesheet(ast.root);
    }
///*
    private void checkStylesheet(Stylesheet stylesheet) {
        for (var child : stylesheet.getChildren()) {
            if (child instanceof VariableAssignment) {
                checkVariableAssignment((VariableAssignment) child);
            }

            if (child instanceof Stylerule) {
                checkStylerule((Stylerule) child);
            }
        }
    }

    private void checkVariableAssignment(VariableAssignment node) {
        for (var child : node.getChildren()) {
            if (child instanceof Operation) {
                checkOperation((Operation) child);
            }
        }
    }

    private void checkOperation(Operation node) {

    }

    private void checkStylerule(Stylerule node) {
        for (ASTNode child : node.getChildren()) {
            if (child instanceof IfClause) {
                checkIfClause((IfClause) child);
            }
            if (child instanceof Declaration) {
                checkDeclaration((Declaration) child);
            }
            if (child instanceof VariableAssignment) {
                checkVariableAssignment((VariableAssignment) child);
            }
        }
    }

    private void checkIfClause(IfClause node) {

    }

    private void checkDeclaration(Declaration node) {
        switch (node.property.name) {
            case "color":
                if (node.expression instanceof Operation) {
                    checkOperation((Operation) node.expression);
                }
                else if (!(node.expression instanceof ColorLiteral)) {
                    node.setError("Property 'color' has invalid type");
                }
                break;
            case "background-color":
                if (node.expression instanceof Operation) {
                    checkOperation((Operation) node.expression);
                }
                else if (!(node.expression instanceof ColorLiteral)) {
                    node.setError("Property 'background-color' has invalid type");
                }
                break;
            case "width":
                if (node.expression instanceof Operation) {
                    checkOperation((Operation) node.expression);
                }
                else if (!(node.expression instanceof PixelLiteral || node.expression instanceof PercentageLiteral)) {
                    node.setError("Property 'width' has invalid type");
                }
                break;
            case "height":
                if (node.expression instanceof Operation) {
                    checkOperation((Operation) node.expression);
                }
                else if (!(node.expression instanceof PixelLiteral || node.expression instanceof PercentageLiteral)) {
                    node.setError("Property 'height' has invalid type");
                }
            default:
                node.setError("Property does not exist");
                break;
        }
    }
//    */
}
