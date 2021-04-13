package com.sugardaddy.citascasualesconocergentenuevacercadeti.models.others;


public class UploadModel {

    private boolean isSelected = false;

    public String imageUrl;

    private String source;
    private String standard_resolution;
    private String photoPath;


    public String getImageUrl() {

        if (source != null && !source.isEmpty()){
            return source;

        } else if (standard_resolution != null && !standard_resolution.isEmpty()){

            return standard_resolution;

        } else if (photoPath != null && !photoPath.isEmpty()){

            return photoPath;

        } else return imageUrl;

    }

    public void setFacebookUrl(String facebookUrl) {
        this.source = facebookUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.standard_resolution = instagramUrl;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
