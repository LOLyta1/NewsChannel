package com.hfad.news.tsivileva.newschannel.Model.implementation.habr;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
* Класс для парсинга блока <!CDATA в XML на Habr
*/
public class HabrItemsDetailConverter implements Converter<HabrItemsDetail> {


        @Override
        public HabrItemsDetail read(InputNode node) throws Exception {
            final String IMG_SRC_REG_EX = "<img src=([^>]+)>";
            final String HTML_TAG_REG_EX = "</?[^>]+>";
            final String HTML_SPACE="     ";

            String nodeText = node.getValue();

            Pattern imageLinkPattern = Pattern.compile(IMG_SRC_REG_EX);
            Matcher matcher = imageLinkPattern.matcher(nodeText);

            String image = null;
            while (matcher.find()) {
                image = matcher.group(1);
            }


            if(image!=null) {

                image.replaceAll(HTML_SPACE,"");

                image = image.substring(1, image.length() - 1);
                if(image.contains("\"")){
                    image = image.substring(0, image.indexOf("\""));
                }

            }


            String desc = nodeText.replaceFirst(IMG_SRC_REG_EX, "")
                    .replaceAll(HTML_TAG_REG_EX, "").replaceAll( HTML_SPACE,"")
                    .replaceAll("Читать дальше →</a>","").
                            replaceAll("Читать дальше →","");

           if(desc.contains("]]>")){
               desc=desc.substring(0,desc.indexOf("]]>"));
           }

            return new HabrItemsDetail(image,desc);


        }

    @Override
    public void write(OutputNode node, HabrItemsDetail value) throws Exception {

    }


}
