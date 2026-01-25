namespace BrasilBurger.Client.Web.ViewModels.Shared;

public sealed class SelectOptionVm
{
    public SelectOptionVm(string value, string text)
    {
        Value = value;
        Text = text;
    }

    public string Value { get; }
    public string Text { get; }
}

