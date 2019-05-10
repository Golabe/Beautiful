package club.goture.beautiful.beautifullib.callback;

public interface OnCompressResultListener {

    void onSuccess(String path);

    void onFailure(String path, String e);

}
