package fr.pinguet62.xjc.javadoc.option;

import com.sun.codemodel.JDocComment;

// TODO APPEND_END
public enum Strategy {

    APPEND_BEGIN {
        @Override
        public void apply(JDocComment javadoc, String documentation) {
            javadoc.append(documentation);
        }
    },
    REPLACE {
        @Override
        public void apply(JDocComment javadoc, String documentation) {
            javadoc.clear();
            javadoc.append(documentation);
        }
    };

    /**
     * Apply the javadoc on {@link JDocComment}.
     *
     * @param javadoc       The {@link JDocComment} to modify.
     * @param documentation The javadoc of component (class, field, method, ...).
     */
    public abstract void apply(JDocComment javadoc, String documentation);

}
