package com.caseo.word.util;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.FooterPart;
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.relationships.Relationships;

import java.util.Map;

public class HeaderFooterUtil {

    public void processHeadersAndFooters(WordprocessingMLPackage wordprocessingPackage,
                                         Map<String, String> replacements) throws Exception {

        Relationships relationships =
                wordprocessingPackage.getMainDocumentPart()
                        .getRelationshipsPart()
                        .getContents();

        for (Relationship relationship : relationships.getRelationship()) {

            if (relationship.getType().equals(
                    org.docx4j.openpackaging.parts.relationships.Namespaces.HEADER)) {

                HeaderPart headerPart =
                        (HeaderPart) wordprocessingPackage.getMainDocumentPart()
                                .getRelationshipsPart()
                                .getPart(relationship);

                headerPart.variableReplace(replacements);

            } else if (relationship.getType().equals(
                    org.docx4j.openpackaging.parts.relationships.Namespaces.FOOTER)) {

                FooterPart footerPart =
                        (FooterPart) wordprocessingPackage.getMainDocumentPart()
                                .getRelationshipsPart()
                                .getPart(relationship);

                footerPart.variableReplace(replacements);
            }
        }
    }
}