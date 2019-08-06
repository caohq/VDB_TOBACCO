package cn.csdb.portal.utils;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.util.*;

/**
 * @Author jinbao
 * @Date 2019/1/28 14:26
 * @Description 基于事件解析excel 生成List<List<String>>格式的对象中转excel数据
 * 不在受限与excel的大小（支持超大excel数据的导入），执行速度明显提高
 **/
public class Excel2ListIncludeNull {

    private static StylesTable stylesTable;

    /**
     * 可解析多sheet页
     *
     * @param fileName 待解析Excel绝对路径
     * @return
     * @throws Exception
     */
    public Map<String, List<List<String>>> processSheet(String fileName) throws Exception {
        Map<String, List<List<String>>> map = new HashMap<>();
        OPCPackage pkg = OPCPackage.open(fileName);
        XSSFReader r = new XSSFReader(pkg);
        stylesTable = r.getStylesTable();
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);

        // 获取sheet相关属性
        XSSFReader.SheetIterator sheetsData = (XSSFReader.SheetIterator) r.getSheetsData();
        while (sheetsData.hasNext()) {
            InputStream next = sheetsData.next();
            InputSource sheetSource = new InputSource(next);
            String sheetName = sheetsData.getSheetName();
            parser.parse(sheetSource);
            SheetHandler contentHandler = (SheetHandler) parser.getContentHandler();

            // 分割sheet页
            List<List<String>> allList = new LinkedList<>();
            allList = contentHandler.getAllList();
            contentHandler.setAllList2Null();
            map.put(sheetName, allList);
        }
        pkg.close();
        return map;
    }

    /**
     * 获取自定义解析器
     */
    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
//        XMLReader parser = XMLReaderFactory.createXMLReader();
        ContentHandler handler = new SheetHandler(sst);
        parser.setContentHandler(handler);
        return parser;
    }

    /**
     * 自定义解析器
     */
    private static class SheetHandler extends DefaultHandler {

        private final DataFormatter formatter = new DataFormatter();
        private SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;
        private List<String> rowList = new ArrayList<String>(10);
        private int curRow = 0;
        private int curCol = 0;
        private List<List<String>> allList = new ArrayList<List<String>>(1024);
        //定义前一个元素和当前元素位置，用来计算空值占位。
        private String preRef = null, ref = null;
        //定义文档一行最大的单元数，补充一行的最后一个空位。
        private String maxRef = null;
        private CellDataType nextDataType = CellDataType.SSTINDEX;
        private short formatIndex;
        private String formatString;

        private SheetHandler(SharedStringsTable sst) {
            this.sst = sst;
        }

        public List<List<String>> getAllList() {
            return allList;
        }

        private void setAllList2Null() {
            this.allList = new ArrayList<List<String>>(1024);
        }

        /**
         * 解析 element开始前触发的事件
         */
        public void startElement(String uri, String localName, String name, Attributes attributes) {
            // c -> cell 获取单元格位置信息
            if ("c".equals(name)) {
                if (preRef == null) {
                    preRef = attributes.getValue("r");
                } else {
                    preRef = ref;
                }

                ref = attributes.getValue("r");
                this.setNextDataType(attributes);

                String cellType = attributes.getValue("t");
                if (cellType != null && "s".equals(cellType)) {
                    nextIsString = true;
                } else {
                    nextIsString = false;
                }
            }
            lastContents = "";
        }

        /**
         * 根据element属性设置数据的类型
         */
        void setNextDataType(Attributes attributes) {
            nextDataType = CellDataType.NUMBER;
            formatIndex = -1;
            formatString = null;
            String cellType = attributes.getValue("t");
            String cellStyleStr = attributes.getValue("s");
            if ("b".equals(cellType)) {
                nextDataType = CellDataType.BOOL;
            } else if ("e".equals(cellType)) {
                nextDataType = CellDataType.ERROR;
            } else if ("inlineStr".equals(cellType)) {
                nextDataType = CellDataType.INLINESTR;
            } else if ("s".equals(cellType)) {
                nextDataType = CellDataType.SSTINDEX;
            } else if ("str".equals(cellType)) {
                nextDataType = CellDataType.FORMULA;
            }

            if (cellStyleStr != null) {
                int styleIndex = Integer.parseInt(cellStyleStr);
                XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
                formatIndex = style.getIndex();
                formatString = style.getDataFormatString();
                if ("m/d/yy".equals(formatString)) {
                    nextDataType = CellDataType.DATE;
                    formatString = "yyyy-MM-dd";
                }
                if (formatString == null) {
                    nextDataType = CellDataType.NULL;
                    formatString = BuiltinFormats.getBuiltinFormat(formatIndex);
                }
            }
        }

        public void endElement(String uri, String localName, String name) {
            if (nextIsString) {
                int idx = Integer.parseInt(lastContents);
                lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
                nextIsString = false;
            }
            if ("v".equals(name)) {
                String value = this.getDataValue(lastContents.trim(), "");
                // 补全单元格之间的空值
                if (!ref.equals(preRef)) {
                    int len = countNullCell(ref, preRef);
                    for (int i = 0; i < len; i++) {
                        rowList.add(curCol, "");
                        curCol++;
                    }
                } else {
                    String s = ref.replaceAll("\\d+", "");
                    // 补全每行开始单元格为空的情况
                    if (!s.equals("A")) {
                        char c = s.toCharArray()[0];
                        int x = c - 'A';
                        for (int i = 0; i < x; i++) {
                            rowList.add(i, "");
                            curCol++;
                        }
                    }
                }
                rowList.add(curCol, value);
                curCol++;
            } else {
                // 如果标签名称为 row,表示已到行尾，调用 optRows()
                if ("row".equals(name)) {
                    String value = "";
                    // 默认第一行为表头，以改行单元格数目为最大数目
                    if (curRow == 0) {
                        maxRef = ref;
                    }

                    // 补全一行尾部可能的空值
                    if (maxRef != null) {
                        int len = countNullCell(maxRef, ref);
                        for (int i = 0; i <= len; i++) {
                            rowList.add(curCol, "");
                            curCol++;
                        }
                    }
                    // 一行结束
                    curRow++;
                    ArrayList<String> strings = new ArrayList<>(rowList);
                    allList.add(strings);
                    // 重置一些数据
                    rowList.clear();
                    curCol = 0;
                    preRef = null;
                    ref = null;
                }
            }

        }

        /**
         * 根据数据类型获取数据
         */
        String getDataValue(String value, String thisStr) {
            switch (nextDataType) {
                case BOOL:
                    char first = value.charAt(0);
                    thisStr = first == '0' ? "FALSE" : "TRUE";
                    break;
                case ERROR:
                    thisStr = "\"ERROR:" + value.toString() + "\"";
                    break;
                case FORMULA:
                    thisStr = "\"" + value.toString() + "\"";
                    break;
                case INLINESTR:
                    XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
                    thisStr = rtsi.toString();
                    break;
                case SSTINDEX:
                    thisStr = value.toString();
                    break;
                case NUMBER:
                    /*if (formatString != null) {
                        thisStr = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString).trim();
                    } else {
                        thisStr = value;
                    }*/
//                    thisStr = thisStr.replace("_", "").trim();
                    thisStr = value.toString();
                    break;
                case DATE:
                    try {
                        thisStr = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString);
                    } catch (NumberFormatException ex) {
                        thisStr = value.toLowerCase();
                    }
                    thisStr = thisStr.replace(" ", "");
                    break;
                default:
                    thisStr = "";
                    break;
            }
            return thisStr;
        }

        int countNullCell(String ref, String preRef) {
            //excel2007最大行数是1048576，最大列数是16384，最后一列列名是XFD
            String xfd = ref.replaceAll("\\d+", "");
            String xfd_1 = preRef.replaceAll("\\d+", "");
            xfd = fillChar(xfd, 3, '@', true);
            xfd_1 = fillChar(xfd_1, 3, '@', true);
            char[] letter = xfd.toCharArray();
            char[] letter_1 = xfd_1.toCharArray();
            int res = (letter[0] - letter_1[0]) * 26 * 26 + (letter[1] - letter_1[1]) * 26 + (letter[2] - letter_1[2]);
            return res - 1;
        }

        /**
         * 字符串的填充
         */
        String fillChar(String str, int len, char let, boolean isPre) {
            int len_1 = str.length();
            if (len_1 < len) {
                if (isPre) {
                    for (int i = 0; i < (len - len_1); i++) {
                        str = let + str;
                    }
                } else {
                    for (int i = 0; i < (len - len_1); i++) {
                        str = str + let;
                    }
                }
            }
            return str;
        }

        public void characters(char[] ch, int start, int length) {
            lastContents = new String(ch, start, length);
        }

        // enum表示单元格可能的数据类型
        enum CellDataType {
            BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER, DATE, NULL
        }

    }

}
