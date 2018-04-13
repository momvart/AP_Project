package views.dialogs;

import java.util.HashMap;

public class DialogResult
{
    private DialogResultCode resultCode;
    private HashMap<String, Object> data;

    public DialogResult(DialogResultCode resultCode)
    {
        this.resultCode = resultCode;
    }

    public DialogResultCode getResultCode()
    {
        return resultCode;
    }

    public DialogResult addData(String key, Object value)
    {
        if (data == null)
            data = new HashMap<>();
        data.put(key, value);
        return this;
    }

    public Object getData(String key)
    {
        return data.get(key);
    }
}
