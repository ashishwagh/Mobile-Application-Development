package com.assignment5.ashish.myapplication;

import java.io.Serializable;


public class NewsList implements Serializable {

        private String name;
        private String id;
        private String category;
        private String author;
        private String title;
        private String description;
        private String imageUrl;
        private String url;
        private String publishedAt;


        public NewsList(String ident, String na, String cat){
            this.name = na;
            this.category = cat;
            this.id = ident;
        }

        public NewsList(String au, String tit, String des, String img, String url, String time){
            this.author = au;
            this.title = tit;
            this.description = des;
            this.imageUrl = img;
            this.url = url;
            this.publishedAt = time;
    }

        public String getId() {
            return id;
        }

        public  void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public String getCategory() {
            return category;
        }

        public void setName(String na) {
            this.name = na;
        }

        public void setCategory(String cat) {
            this.category = cat;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String auth) {
            this.author = auth;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String tit) {
            this.title = tit;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String des) {
            this.description = des;
        }

        public String getimageUrl() {
            return imageUrl;
        }

        public void setimageUrl(String img) {
            this.imageUrl = img;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String ur) {
            this.url = ur;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String time) {
            this.publishedAt = time;
        }


}
