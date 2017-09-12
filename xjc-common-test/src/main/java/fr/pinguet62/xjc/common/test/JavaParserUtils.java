package fr.pinguet62.xjc.common.test;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;

public class JavaParserUtils {

    /**
     * @param enumConstant
     *            The {@link BodyDeclaration} on which search.
     * @param annotation
     *            The {@link AnnotationExpr#getName() annotation name} type to search.
     * @return The first found (doesn't support {@link Repeatable}).<br>
     *         {@code null} if not found.
     */
    public static AnnotationExpr findAnnotation(BodyDeclaration<?> enumConstant, Class<? extends Annotation> annotation) {
        for (AnnotationExpr a : enumConstant.getAnnotations())
            if (a.getName().getIdentifier().equals(annotation.getSimpleName()))
                return a;
        return null;
    }

    /**
     * @param type
     *            The {@link TypeDeclaration} on which search.
     * @param name
     *            The {@link VariableDeclarator#getName() field name} to search.
     * @param annotation
     *            The {@link AnnotationExpr#getName() annotation name} type to search.
     * @return The first found (doesn't support {@link Repeatable}).<br>
     *         {@code null} if not found.
     */
    public static AnnotationExpr findFieldAnnotation(TypeDeclaration<?> type, String name, Class<? extends Annotation> annotation) {
        for (BodyDeclaration<?> member : type.getMembers())
            if (member instanceof FieldDeclaration) {
                FieldDeclaration field = (FieldDeclaration) member;
                for (VariableDeclarator variableDeclarator : field.getVariables())
                    if (variableDeclarator.getName().getIdentifier().equals(name)) {
                        for (AnnotationExpr a : field.getAnnotations())
                            if (a.getName().getIdentifier().equals(annotation.getSimpleName()))
                                return a;
                        return null;
                    }
            }
        return null;
    }

    /**
     * @param type
     *            The {@link TypeDeclaration} on which search.
     * @param name
     *            The {@link VariableDeclarator#getName() field name} to search.
     * @return The first found (doesn't support {@link Repeatable}).<br>
     *         {@code null} if not found.
     */
    public static Comment findFieldComment(TypeDeclaration<?> type, String name) {
        if (type instanceof EnumDeclaration) {
            EnumDeclaration enumDeclaration = (EnumDeclaration) type;
            for (EnumConstantDeclaration enumConstantDeclaration : enumDeclaration.getEntries())
                if (enumConstantDeclaration.getName().getIdentifier().equals(name))
                    return enumConstantDeclaration.getComment().orElse(null);
        } else
            for (BodyDeclaration<?> member : type.getMembers())
                if (member instanceof FieldDeclaration) {
                    FieldDeclaration field = (FieldDeclaration) member;
                    for (VariableDeclarator variableDeclarator : field.getVariables())
                        if (variableDeclarator.getName().getIdentifier().equals(name))
                            return variableDeclarator.getComment().orElse(field.getComment().orElse(null));
                }
        return null;
    }

    /**
     * @param annotation
     *            The {@link AnnotationExpr} on which search.
     * @param key
     *            The {@link MemberValuePair#getName() annotation parameter name} to search.
     * @return {@code null} if not found.<br>
     *         {@code null} if invalid parameter name.
     */
    public static Expression findArgument(AnnotationExpr annotation, String key) {
        if (annotation instanceof MarkerAnnotationExpr)
            return null;
        else if (annotation instanceof SingleMemberAnnotationExpr) {
            SingleMemberAnnotationExpr singleMemberAnnotation = (SingleMemberAnnotationExpr) annotation;
            if (!key.equals("value")) // name for annotation with single parameter
                return null;
            return singleMemberAnnotation.getMemberValue();
        } else if (annotation instanceof NormalAnnotationExpr) {
            NormalAnnotationExpr normalAnnotation = (NormalAnnotationExpr) annotation;
            for (MemberValuePair memberValue : normalAnnotation.getPairs())
                if (memberValue.getName().getIdentifier().equals(key))
                    return memberValue.getValue();
            return null;
        } else
            throw new UnsupportedOperationException("Unknown annotation type: " + annotation.getClass());
    }

    /**
     * @param type
     *            The {@link EnumDeclaration} on which search.
     * @param entry
     *            The {@link EnumConstantDeclaration#getName() constant name} to search.
     * @return {@code null} if not found.
     */
    public static EnumConstantDeclaration findEntry(EnumDeclaration type, String entry) {
        for (EnumConstantDeclaration enumConstant : type.getEntries())
            if (enumConstant.getName().getIdentifier().equals(entry))
                return enumConstant;
        return null;
    }

    /**
     * @param type
     *            The {@link TypeDeclaration} on which search.
     * @param name
     *            The {@link VariableDeclarator#getName() field name} to search.
     * @return {@code null} if not found.
     */
    public static VariableDeclarator findField(TypeDeclaration<?> type, String name) {
        for (BodyDeclaration<?> member : type.getMembers())
            if (member instanceof FieldDeclaration) {
                FieldDeclaration field = (FieldDeclaration) member;
                for (VariableDeclarator variableDeclarator : field.getVariables())
                    if (variableDeclarator.getName().getIdentifier().equals(name))
                        return variableDeclarator;
            }
        return null;
    }

    /**
     * @param type
     *            The {@link TypeDeclaration} on which search.
     * @param name
     *            The {@link MethodDeclaration#getName() method name} to search.
     * @return The first found (naming collision).<br>
     *         {@code null} if not found.
     */
    public static MethodDeclaration findMethod(TypeDeclaration<?> type, String name) {
        for (BodyDeclaration<?> member : type.getMembers())
            if (member instanceof MethodDeclaration) {
                MethodDeclaration method = (MethodDeclaration) member;
                if (method.getName().getIdentifier().equals(name))
                    return method;
            }
        return null;
    }

    /**
     * Remove all {@code "*"} and empty first &amp; last lines.
     *
     * @param comment
     *            The initial text.
     * @return The formated text.
     */
    public static final List<String> formatComments(Comment comment) {
        String[] lines = comment.getContent().split("\r?\n");
        List<String> comments = new ArrayList<>();
        for (int i = 1; i < lines.length - 2; i++)
            comments.add(lines[i].replaceFirst("^(    )? \\* ", ""));
        return comments;
    }

}