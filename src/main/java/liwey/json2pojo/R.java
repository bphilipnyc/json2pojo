package liwey.json2pojo;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Resources helper, name like android.
 */
public class R {
  public static String get(String key, Object... params) {
    ResourceBundle bundle = ResourceBundle.getBundle("bundles", Locale.getDefault());
    return MessageFormat.format(bundle.getString(key), params);
  }
}
