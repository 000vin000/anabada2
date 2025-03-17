package kr.co.anabada.main.entity;

 public class SearchRequest {
        private String findType;
        private String keyword;

        public String getFindType() {
            return findType;
        }

        public void setFindType(String findType) {
            this.findType = findType;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }
    }