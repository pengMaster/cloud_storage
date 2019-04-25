package king.cloud.camara.base;

import android.support.v4.app.Fragment;

import king.cloud.camara.frag.FolderListFrag;
import king.cloud.camara.frag.MyFrag;
import king.cloud.camara.frag.NoteListFrag;
import king.cloud.camara.iface.FragmentConstant;


/**
 * @author wush
 * @version v1.0
 * @date 2015-12-8
 */
public class FragmentFactory implements FragmentConstant {

    public static Fragment getInstanceByIndex(int index) {
        Fragment fragment = null;
        switch (index) {
            case frag_file_dir:
                fragment = new FolderListFrag();
                break;
            case frag_note:
                fragment = new NoteListFrag();
                break;
            case frag_my:
                fragment = new MyFrag();
                break;
            default:
                fragment = new Fragment();
                break;
        }
        return fragment;
    }
}
