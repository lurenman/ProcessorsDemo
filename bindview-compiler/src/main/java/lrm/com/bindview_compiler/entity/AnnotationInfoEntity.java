package lrm.com.bindview_compiler.entity;

import java.util.HashMap;

import javax.lang.model.element.VariableElement;

/**
 * 创建日期：2019/6/15
 * 作者:baiyang
 * 保存相关注解 所属的类名，包名
 * 及variableElement map对象
 */
public class AnnotationInfoEntity {
    /*包名*/
    private String pk;
    /*类名*/
    private String clsName;
    /*注解对象*/
    private HashMap<Integer,VariableElement> varMap;

    public AnnotationInfoEntity(String pk, String clsName) {
        this.pk = pk;
        this.clsName = clsName;
        varMap=new HashMap<>();
    }

    public String getPk() {
        return pk;
    }

    public AnnotationInfoEntity setPk(String pk) {
        this.pk = pk;
        return this;
    }

    public String getClsName() {
        return clsName;
    }

    public AnnotationInfoEntity setClsName(String clsName) {
        this.clsName = clsName;
        return this;
    }

    public HashMap<Integer, VariableElement> getVarMap() {
        return varMap;
    }

    public AnnotationInfoEntity setVarMap(HashMap<Integer, VariableElement> varMap) {
        this.varMap = varMap;
        return this;
    }
}
