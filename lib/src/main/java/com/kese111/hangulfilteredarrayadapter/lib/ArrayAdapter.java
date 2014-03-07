package com.kese111.hangulfilteredarrayadapter.lib;

/**
 * Created by IamI on 2014. 3. 7..
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class ArrayAdapter<T> extends BaseAdapter {

    final static char[] ChoSung = { 0x3131, 0x3132, 0x3134, 0x3137, 0x3138,
            0x3139, 0x3141, 0x3142, 0x3143, 0x3145, 0x3146, 0x3147, 0x3148,
            0x3149, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e };

    final static char[] JwungSung = { 0x314f, 0x3150, 0x3151, 0x3152, 0x3153,
            0x3154, 0x3155, 0x3156, 0x3157, 0x3158, 0x3159, 0x315a, 0x315b,
            0x315c, 0x315d, 0x315e, 0x315f, 0x3160, 0x3161, 0x3162, 0x3163 };

    final static char[] JongSung = { 0, 0x3131, 0x3132, 0x3133, 0x3134, 0x3135,
            0x3136, 0x3137, 0x3139, 0x313a, 0x313b, 0x313c, 0x313d, 0x313e,
            0x313f, 0x3140, 0x3141, 0x3142, 0x3144, 0x3145, 0x3146, 0x3147,
            0x3148, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e };


    /**
     * Contains the list of objects that represent the data of this ArrayAdapter.
     * The content of this list is referred to as "the array" in the documentation.
     */
    protected List<T> mObjects;
    protected ArrayList<T> mOriginalValues;
    protected ArrayFilter mFilter;
    protected final Object mLock = new Object();
    private int mResource;

    private boolean mNotifyOnChange = true;
    private int mFieldId = 0;
    private Context mContext;
    private LayoutInflater mInflater;
    private int mDropDownResource;

    /**
     * Constructor
     *
     * @param context The current context.
     * @param textViewResourceId The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     */
    public ArrayAdapter(Context context, int textViewResourceId) {
        init(context, textViewResourceId, 0, new ArrayList<T>());
    }

    /**
     * Constructor
     *
     * @param context The current context.
     * @param resource The resource ID for a layout file containing a layout to use when
     *                 instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated
     */
    public ArrayAdapter(Context context, int resource, int textViewResourceId) {
        init(context, resource, textViewResourceId, new ArrayList<T>());
    }

    /**
     * Constructor
     *
     * @param context The current context.
     * @param textViewResourceId The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects The objects to represent in the ListView.
     */
    public ArrayAdapter(Context context, int textViewResourceId, T[] objects) {
        init(context, textViewResourceId, 0, Arrays.asList(objects));
    }

    /**
     * Constructor
     *
     * @param context The current context.
     * @param resource The resource ID for a layout file containing a layout to use when
     *                 instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated
     * @param objects The objects to represent in the ListView.
     */
    public ArrayAdapter(Context context, int resource, int textViewResourceId, T[] objects) {
        init(context, resource, textViewResourceId, Arrays.asList(objects));
    }

    /**
     * Constructor
     *
     * @param context The current context.
     * @param textViewResourceId The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects The objects to represent in the ListView.
     */
    public ArrayAdapter(Context context, int textViewResourceId, List<T> objects) {
        init(context, textViewResourceId, 0, objects);
    }

    /**
     * Constructor
     *
     * @param context The current context.
     * @param resource The resource ID for a layout file containing a layout to use when
     *                 instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated
     * @param objects The objects to represent in the ListView.
     */
    public ArrayAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
        init(context, resource, textViewResourceId, objects);
    }


    /**
     * Creates a new {@link ArrayAdapter} with a <b>copy</b> of the specified
     * list, or an empty list if items == null.
     */

    public ArrayAdapter(List<T> items) {
        mObjects = new ArrayList<T>();
        if (items != null) {
            mObjects.addAll(items);
        }
    }


    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public T getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mResource);
    }

    private View createViewFromResource(int position, View convertView, ViewGroup parent,
                                        int resource) {
        View view;
        TextView text;

        if (convertView == null) {
            view = mInflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        try {
            if (mFieldId == 0) {
                //  If no custom field is assigned, assume the whole resource is a TextView
                text = (TextView) view;
            } else {
                //  Otherwise, find the TextView field within the layout
                text = (TextView) view.findViewById(mFieldId);
            }
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }

        text.setText(getItem(position).toString());

        return view;
    }

    /**
     * Appends the specified element to the end of the list.
     */
    // @ requires item != null;
    public void add(T item) {
        mObjects.add(item);
        notifyDataSetChanged();
    }

    /**
     * Inserts the specified element at the specified position in the list.
     */
    public void add(int position, T item) {
        mObjects.add(position, item);
        notifyDataSetChanged();
    }

    /**
     * Appends all of the elements in the specified collection to the end of the
     * list, in the order that they are returned by the specified collection's
     * Iterator.
     */
    public void addAll(Collection<? extends T> items) {
        mObjects.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * Appends all of the elements to the end of the list, in the order that
     * they are specified.
     */
    public void addAll(T... items) {
        Collections.addAll(mObjects, items);
        notifyDataSetChanged();
    }

    /**
     * Inserts all of the elements in the specified collection into the list,
     * starting at the specified position.
     */
    public void addAll(int position, Collection<? extends T> items) {
        mObjects.addAll(position, items);
        notifyDataSetChanged();
    }

    /**
     * Inserts all of the elements into the list, starting at the specified
     * position.
     */
    public void addAll(int position, T... items) {
        for (int i = position; i < (items.length + position); i++) {
            mObjects.add(i, items[i]);
        }
        notifyDataSetChanged();
    }

    /**
     * Removes all of the elements from the list.
     */
    public void clear() {
        mObjects.clear();
        notifyDataSetChanged();
    }

    /**
     * Replaces the element at the specified position in this list with the
     * specified element.
     */
    public void set(int position, T item) {
        mObjects.set(position, item);
        notifyDataSetChanged();
    }

    /**
     * Removes the specified element from the list
     */
    public void remove(T item) {
        mObjects.remove(item);
        notifyDataSetChanged();
    }

    /**
     * Removes the element at the specified position in the list
     */
    public void remove(int position) {
        mObjects.remove(position);
        notifyDataSetChanged();
    }

    /**
     * Removes all elements at the specified positions in the list
     */
    public void removePositions(Collection<Integer> positions) {
        ArrayList<Integer> positionsList = new ArrayList<Integer>(positions);
        Collections.sort(positionsList);
        Collections.reverse(positionsList);
        for (int position : positionsList) {
            mObjects.remove(position);
        }
        notifyDataSetChanged();
    }

    /**
     * Removes all of the list's elements that are also contained in the
     * specified collection
     */
    public void removeAll(Collection<T> items) {
        mObjects.removeAll(items);
        notifyDataSetChanged();
    }

    /**
     * Retains only the elements in the list that are contained in the specified
     * collection
     */
    public void retainAll(Collection<T> items) {
        mObjects.retainAll(items);
        notifyDataSetChanged();
    }

    /**
     * Returns the position of the first occurrence of the specified element in
     * this list, or -1 if this list does not contain the element. More
     * formally, returns the lowest position <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
     * or -1 if there is no such position.
     */
    public int indexOf(T item) {
        return mObjects.indexOf(item);
    }

    private void init(Context context, int resource, int textViewResourceId, List<T> objects) {
        mContext = context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource = mDropDownResource = resource;
        mObjects = objects;
        mFieldId = textViewResourceId;
    }

    private BaseAdapter mDataSetChangedSlavedAdapter;

    public void propagateNotifyDataSetChanged(BaseAdapter slavedAdapter) {
        mDataSetChangedSlavedAdapter = slavedAdapter;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (mDataSetChangedSlavedAdapter != null) {
            mDataSetChangedSlavedAdapter.notifyDataSetChanged();
        }
    }

    /**
     * <p>Sets the layout resource to create the drop down views.</p>
     *
     * @param resource the layout resource defining the drop down views
     * @see #getDropDownView(int, android.view.View, android.view.ViewGroup)
     */
    public void setDropDownViewResource(int resource) {
        this.mDropDownResource = resource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mDropDownResource);
    }


    /**
     * {@inheritDoc}
     */
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    /**
     * <p>An array filter constrains the content of the array adapter with
     * a prefix. Each item that does not start with the supplied prefix
     * is removed from the list.</p>
     */
    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<T>(mObjects);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<T> list;
                synchronized (mLock) {
                    list = new ArrayList<T>(mOriginalValues);
                }                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();
                ArrayList<T> values;
                synchronized (mLock) {
                    values = new ArrayList<T>(mOriginalValues);
                }

                final int count = values.size();
                final ArrayList<T> newValues = new ArrayList<T>();

                for (int i = 0; i < count; i++) {
                    final T value = values.get(i);
                    String valueText = value.toString().toLowerCase();
                    if (isContainsHangul(prefixString) && isContainsHangul(valueText)) {
                        prefixString = convertToHangulJasoString(prefixString);
                        valueText = convertToHangulJasoString(valueText);
                    }

                    // First match against the whole, non-splitted value
                    if (valueText.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        if (valueText.matches(".*"+prefixString+".*")) {
                            newValues.add(value);
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mObjects = (List<T>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    public boolean isHangul(Character arg) {
        Character.UnicodeBlock unicodeBlock = Character.UnicodeBlock.of(arg);
        if(Character.UnicodeBlock.HANGUL_SYLLABLES.equals(unicodeBlock) ||
                Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO.equals(unicodeBlock) ||
                Character.UnicodeBlock.HANGUL_JAMO.equals(unicodeBlock)) {
            return true;
        }
        return false;
    }

    public boolean isContainsHangul(String str)
    {
        for(int i = 0 ; i < str.length() ; i++)
        {
            char ch = str.charAt(i);
            if(isHangul(ch))
                return true;
        }
        return false;
    }

    public String convertToHangulJasoString(String str) {
        List<String> result = new ArrayList<String>();
        String[] array = str.split("");
        for (int i = 1 ; i < array.length ; i++ ) {
            result.add(array[i]);
        }

        for (int i = 0; i < result.size(); i++) {
            char ch = result.get(i).charAt(0);
            if(isHangul(ch)) {
                result.remove(i);
                result.add(i, hangulToJaso(ch));
            }
        }

        StringBuffer strBuf = new StringBuffer();
        for(String e : result) {
            strBuf.append(e);
        }
        return strBuf.toString();
    }

    public static String hangulToJaso(char s) {
        int a, b, c;
        String result = "";

        char ch = s;

        if (ch >= 0xAC00 && ch <= 0xD7A3) {
            c = ch - 0xAC00;
            a = c / (21 * 28);
            c = c % (21 * 28);
            b = c / 28;
            c = c % 28;

            result = result + ChoSung[a] + JwungSung[b];
            if (c != 0)
                result = result + JongSung[c];
        } else {
            result = result + ch;
        }
        return result;
    }

}

