package com.caseo.word.render.word.docx4j;

import org.docx4j.XmlUtils;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.FooterPart;
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.relationships.Relationships;
import org.docx4j.wml.*;

import java.io.File;
import java.math.BigInteger;
import java.util.*;

public class Docx4jTagProcessor {

    public void process(File template,
                        File output,
                        Map<String, Object> data) throws Exception {

        WordprocessingMLPackage wordMLPackage =
                WordprocessingMLPackage.load(template);

        MainDocumentPart mdp = wordMLPackage.getMainDocumentPart();

        VariablePrepare.prepare(wordMLPackage);

        // ===== TEXT =====
        HashMap<String, String> simpleReplacements = new HashMap<>();

        for (Map.Entry<String, Object> entry : data.entrySet()) {

            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof String) {
                simpleReplacements.put(key, (String) value);
            }
        }

        // сначала текст (включая колонтитулы)
        mdp.variableReplace(simpleReplacements);
        processHeadersAndFooters(wordMLPackage, simpleReplacements);

        // ===== LIST + TABLE =====
        for (Map.Entry<String, Object> entry : data.entrySet()) {

            String key = entry.getKey();
            Object value = entry.getValue();

            // LIST
            if (value instanceof String[]) {
                processList(
                        mdp,
                        key,
                        (String[]) value
                );
                continue;
            }

            // TABLE
            if (value instanceof List) {
                @SuppressWarnings("unchecked")
                List<String[]> rows = (List<String[]>) value;

                processTable(
                        mdp,
                        key,
                        rows
                );
            }
        }

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // ===== IMAGE =====
            if (value instanceof byte[]) {
                processImage(mdp, key, (byte[]) value);
                continue;
            }

            // ===== LIST =====
            if (value instanceof String[]) {
                processList(mdp, key, (String[]) value);
                continue;
            }

            // ===== TABLE =====
            if (value instanceof List) {
                @SuppressWarnings("unchecked")
                List<String[]> rows = (List<String[]>) value;
                processTable(mdp, key, rows);
                continue;
            }
        }

        // колонтитулы
        processHeadersAndFooters(wordMLPackage, simpleReplacements);

        // сохранение
        wordMLPackage.save(output);
    }

    private void processImage(MainDocumentPart mdp, String tag, byte[] imageBytes) throws Exception {

        List<Object> paragraphs = getAllElementFromObject(mdp, P.class);

        for (Object pObj : paragraphs) {
            P p = (P) pObj;

            if (containsTag(p, tag)) {

                // удаляем тэг
                p.getContent().clear();

                // создаём картинку
                BinaryPartAbstractImage imagePart =
                        BinaryPartAbstractImage.createImagePart((WordprocessingMLPackage) mdp.getPackage(), imageBytes);

                Inline inline = imagePart.createImageInline(
                        tag,
                        tag,
                        0,
                        1,
                        false
                );

                ObjectFactory factory = Context.getWmlObjectFactory();

                Drawing drawing = factory.createDrawing();
                drawing.getAnchorOrInline().add(inline);

                R run = factory.createR();
                run.getContent().add(drawing);

                p.getContent().add(run);

                break;
            }
        }
    }


    private void processList(MainDocumentPart mdp, String tag, String[] items) {
        List<Object> allP = getAllElementFromObject(mdp, P.class);
        for (Object pObj : allP) {
            P p = (P) pObj;
            if (containsTag(p, tag)) {
                ContentAccessor parent = (ContentAccessor) p.getParent();
                int index = parent.getContent().indexOf(p);
                RPr format = getFirstRPr(p);

                parent.getContent().remove(p);
                for (int i = 0; i < items.length; i++) {
                    String prefix = buildListPrefix(tag, i + 1);

                    parent.getContent().add(
                            index + i,
                            createNumberedP(prefix, items[i], format)
                    );
                }
                break;
            }
        }
    }

    private String buildListPrefix(String tag, int index) {

        // OBJECT_STRUCTURE_LIST1 -> 1.)
        if (tag.endsWith("LIST1")) {
            return index + ".) ";
        }

        // TECHNO_BLOCK_LIST№ -> № 1
        if (tag.endsWith("LIST№")) {
            return "№ " + index + " ";
        }

        // дефолт
        return index + ".) ";
    }

    private boolean containsTag(Object obj, String tag) {
        return XmlUtils.marshaltoString(obj).contains(tag);
    }

    private RPr getFirstRPr(P p) {
        List<Object> runs = getAllElementFromObject(p, R.class);
        if (!runs.isEmpty()) {
            return ((R) runs.get(0)).getRPr();
        }
        return null;
    }

    private List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
        List<Object> result = new ArrayList<>();
        Object unwrapped = XmlUtils.unwrap(obj);

        if (toSearch.isInstance(unwrapped)) {
            result.add(unwrapped);
        } else if (unwrapped instanceof ContentAccessor) {
            for (Object child : ((ContentAccessor) unwrapped).getContent()) {
                result.addAll(getAllElementFromObject(child, toSearch));
            }
        }
        return result;
    }

    private PPr getOrCreatePPr(P p) {
        if (p.getPPr() == null) {
            p.setPPr(Context.getWmlObjectFactory().createPPr());
        }
        return p.getPPr();
    }

    private R createFormattedRun(RPr format) {
        R r = Context.getWmlObjectFactory().createR();
        if (format != null) {
            r.setRPr(XmlUtils.deepCopy(format));
        }
        return r;
    }

    private void applyStandardSpacing(P p) {
        ObjectFactory factory = Context.getWmlObjectFactory();
        PPr ppr = getOrCreatePPr(p);

        PPrBase.Spacing spacing = factory.createPPrBaseSpacing();
        spacing.setBefore(BigInteger.ZERO);
        spacing.setAfter(BigInteger.ZERO);
        spacing.setLine(BigInteger.valueOf(240));
        spacing.setLineRule(STLineSpacingRule.AUTO);
        ppr.setSpacing(spacing);
    }

    private void addTextWithBreaks(ContentAccessor target, String text, RPr format) {
        ObjectFactory factory = Context.getWmlObjectFactory();
        String[] lines = text.split("\n", -1);

        for (int i = 0; i < lines.length; i++) {
            Text t = factory.createText();
            t.setValue(lines[i]);
            t.setSpace("preserve");

            if (target instanceof R) {
                target.getContent().add(t);
                if (i < lines.length - 1) target.getContent().add(factory.createBr());
            } else {
                R newRun = factory.createR();
                if (format != null) newRun.setRPr(XmlUtils.deepCopy(format));
                newRun.getContent().add(t);
                target.getContent().add(newRun);
                if (i < lines.length - 1) newRun.getContent().add(factory.createBr());
            }
        }
    }

    private P createNumberedP(String prefix, String text, RPr format) {
        ObjectFactory factory = Context.getWmlObjectFactory();
        P p = factory.createP();
        applyStandardSpacing(p);

        Tabs tabs = factory.createTabs();
        CTTabStop tabStop = factory.createCTTabStop();
        tabStop.setVal(STTabJc.LEFT);
        tabStop.setPos(BigInteger.valueOf(720));
        tabs.getTab().add(tabStop);
        getOrCreatePPr(p).setTabs(tabs);

        // номер
        R r1 = createFormattedRun(format);
        Text t1 = factory.createText();
        t1.setValue(prefix);
        r1.getContent().add(t1);
        r1.getContent().add(factory.createRTab());
        p.getContent().add(r1);

        // текст
        R r2 = createFormattedRun(format);
        addTextWithBreaks(r2, text, null);
        p.getContent().add(r2);

        return p;
    }

    private void processTable(MainDocumentPart mdp, String tag, List<String[]> data) {
        List<Object> tables = getAllElementFromObject(mdp, Tbl.class);
        for (Object tblObj : tables) {
            Tbl tbl = (Tbl) tblObj;
            Tr templateRow = null;

            for (Object rowObj : tbl.getContent()) {
                if (XmlUtils.marshaltoString(rowObj).contains(tag)) {
                    templateRow = (Tr) XmlUtils.unwrap(rowObj);
                    break;
                }
            }

            if (templateRow != null) {
                int rowIdx = tbl.getContent().indexOf(templateRow);

                for (String[] vals : data) {
                    Tr newRow = XmlUtils.deepCopy(templateRow);
                    List<Object> cells = newRow.getContent();

                    for (int i = 0; i < vals.length; i++) {
                        if (i < cells.size()) {
                            fillCellWithText(
                                    (Tc) XmlUtils.unwrap(cells.get(i)),
                                    tag,
                                    vals[i]
                            );
                        }
                    }

                    tbl.getContent().add(rowIdx + 1, newRow);
                    rowIdx++;
                }

                tbl.getContent().remove(templateRow);
                break;
            }
        }
    }


    private void fillCellWithText(Tc tc, String placeholder, String value) {
        List<Object> paragraphs = getAllElementFromObject(tc, P.class);
        for (Object pObj : paragraphs) {
            P p = (P) pObj;
            applyStandardSpacing(p);

            List<Object> runs = getAllElementFromObject(p, R.class);
            for (Object rObj : runs) {
                R r = (R) rObj;
                String xml = XmlUtils.marshaltoString(r);
                if (xml.contains(placeholder)) {
                    RPr currentFormat = r.getRPr();
                    r.getContent().clear();
                    addTextWithBreaks(r, value, currentFormat);
                }
            }
        }
    }


    private void processHeadersAndFooters(WordprocessingMLPackage wordMLPackage,
                                          Map<String, String> replacements) throws Exception {

        Relationships rels =
                wordMLPackage.getMainDocumentPart()
                        .getRelationshipsPart()
                        .getContents();

        for (Relationship rel : rels.getRelationship()) {
            if (rel.getType().equals(
                    org.docx4j.openpackaging.parts.relationships.Namespaces.HEADER)) {

                HeaderPart header =
                        (HeaderPart) wordMLPackage.getMainDocumentPart()
                                .getRelationshipsPart()
                                .getPart(rel);

                header.variableReplace(replacements);

            } else if (rel.getType().equals(
                    org.docx4j.openpackaging.parts.relationships.Namespaces.FOOTER)) {

                FooterPart footer =
                        (FooterPart) wordMLPackage.getMainDocumentPart()
                                .getRelationshipsPart()
                                .getPart(rel);

                footer.variableReplace(replacements);
            }
        }
    }
}