package com.example.root.flickrbrowser;

class Photo {
    public String mTitle;
    public String mAuthor;
    public String mAuthorId;
    public String mTags;
    public String mImage;

    public Photo(String title, String author, String authorId, String tags, String image) {
        mTitle = title;
        mAuthor = author;
        mAuthorId = authorId;
        mTags = tags;
        mImage = image;
    }


    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getAuthorId() {
        return mAuthorId;
    }

    public String getTags() {
        return mTags;
    }

    public String getImage() {
        return mImage;
    }


    @Override
    public String toString() {
        return "Photo{" +
                "mTitle='" + mTitle + '\n' +
                ", mAuthor='" + mAuthor + '\n' +
                ", mAuthorId='" + mAuthorId + '\n' +
                ", mTags='" + mTags + '\n' +
                ", mImage='" + mImage + '\n' +
                '}';
    }
}
