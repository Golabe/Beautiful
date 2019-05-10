package club.goture.beautiful.beautifullib.callback;

import java.util.List;

import club.goture.beautiful.beautifullib.bean.Photo;

public interface OnZipListener {

    void onStart();

    void onProgress(int progress);

    void onSuccess(List<Photo> photos);

    void onFailure(List<Photo> photos, String e);

    void onComplete();
}
