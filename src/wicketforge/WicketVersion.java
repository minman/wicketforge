/*
 * Copyright 2010 The WicketForge-Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicketforge;

import com.intellij.openapi.module.Module;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
// TODO think about to remove WicketVersion and implement getDtd and getXmlPropertiesFileExtension in other way...
enum WicketVersion {
    WICKET_1_3("http://wicket.apache.org/dtds.data/wicket-xhtml1.3-strict.dtd", "xml"),
    WICKET_1_4("http://wicket.apache.org/dtds.data/wicket-xhtml1.4-strict.dtd", "xml"),
    WICKET_1_5("http://wicket.apache.org/dtds.data/wicket-xhtml1.4-strict.dtd", "properties.xml"); // at the moment there is no 1.5 nor 6.0 dtd...

    private String dtd;
    private String xmlPropertiesFileExtension;

    private WicketVersion(@NotNull String dtd, @NotNull String xmlPropertiesFileExtension) {
        this.dtd = dtd;
        this.xmlPropertiesFileExtension = xmlPropertiesFileExtension;
    }

    @NotNull
    public String getXmlPropertiesFileExtension() {
        return xmlPropertiesFileExtension;
    }

    @NotNull
    public String getDtd() {
        return dtd;
    }

    @NotNull
    public static WicketVersion getVersion(@Nullable Module module) {
        if (module == null) {
            return WICKET_1_5;
        }
        JavaPsiFacade psiFacade = JavaPsiFacade.getInstance(module.getProject());
        GlobalSearchScope scope = GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module);

        PsiClass c = psiFacade.findClass("org.apache.wicket.Component", scope);
        if (c == null) {
            return WICKET_1_5;
        }

        List<String> methods = new ArrayList<String>();
        for (PsiMethod m : c.getMethods()) {
            methods.add(m.getName());
        }
        if (methods.contains("getMarkup")) {
            return WICKET_1_5;
        } else if (methods.contains("getDefaultModel")) {
            return WICKET_1_4;
        }
        return WICKET_1_3;

    }
}