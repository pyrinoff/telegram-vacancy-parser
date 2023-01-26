package ru.pyrinoff.chatjobparser.util;

import com.sun.codemodel.JCodeModel;
import org.jsonschema2pojo.*;
import org.jsonschema2pojo.rules.RuleFactory;

import java.io.File;
import java.io.IOException;

public interface PojoGenUtil {

    static void convertJsonToJavaClass(String jsonString, File outputJavaClassDirectory, String packageName, String javaClassName) throws IOException {
        JCodeModel jcodeModel = new JCodeModel();
        GenerationConfig config = new DefaultGenerationConfig() {
            @Override
            public boolean isGenerateBuilders() {
                return true;
            }

            @Override
            public SourceType getSourceType() {
                return SourceType.JSON;
            }

            @Override
            public boolean isIncludeHashcodeAndEquals() {
                return false;
            }

            @Override
            public boolean isIncludeToString() {
                return false;
            }

            @Override
            public boolean isIncludeConstructors() {
                return false;
            }

            @Override
            public boolean isIncludeGetters() {
                return false;
            }

            @Override
            public boolean isIncludeSetters() {
                return false;
            }

            @Override
            public boolean isIncludeDynamicAccessors() {
                return false;
            }
            @Override
            public boolean isIncludeAdditionalProperties() {
                return false;
            }

        };

        SchemaMapper mapper = new SchemaMapper(new RuleFactory(config, new Jackson2Annotator(config), new SchemaStore()), new SchemaGenerator());
        mapper.generate(jcodeModel, javaClassName, packageName, jsonString);
        jcodeModel.build(outputJavaClassDirectory);
    }

}
