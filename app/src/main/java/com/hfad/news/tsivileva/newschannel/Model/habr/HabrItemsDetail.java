package com.hfad.news.tsivileva.newschannel.model.habr;
/*
 * Класс для хранения данных о картинке и описании статьи на Habr
 */
public class HabrItemsDetail {
    public HabrItemsDetail() {

    }
         /*ссылка на картинку*/
        private String imageSrc;

        /*описание новости*/
        private String description;

        public HabrItemsDetail(String imageSrc, String description) {
            this.imageSrc = imageSrc;
            this.description = description;
        }


        public String getImageSrc() {
            return imageSrc;
        }

        public void setImageSrc(String imageSrc) {
            this.imageSrc = imageSrc;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }








}
