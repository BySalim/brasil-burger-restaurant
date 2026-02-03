using BrasilBurger.Client.Domain.Enums;
using BrasilBurger.Client.Web.EnumsUi.Options;
using BrasilBurger.Client.Web.EnumsUi.Helpers;

namespace BrasilBurger.Client.Web.EnumsUi.Extensions;

public static class ModePaiementUiExtensions
{
    public static EnumUiMeta Ui(this ModePaiement v) => v switch
    {
        ModePaiement.WAVE => new(
            nameof(ModePaiement.WAVE),
            "Wave",
            new UiImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQm9rYPURKIok7K0ZF22oqFgMbzIHgNCauVQA&s"),
            UiColor.Bleu),

        ModePaiement.OM => new(
            nameof(ModePaiement.OM),
            "Orange Money",
            new UiImage("https://dimelo-answers-production.s3-eu-west-1.amazonaws.com/268/6f44dfa59e0bcf0b/om_logo_original.png?09c0932"),
            UiColor.Orange),

        _ => throw new ArgumentOutOfRangeException(nameof(v), v, "ModePaiement non mappé (UI).")
    };
    
    private static readonly EnumUiMeta[] Items = EnumUiHelper.All<ModePaiement>(Ui);
    
    public static IEnumerable<EnumUiMeta> AllUi() => Items;
    
    public static ModePaiement? ToEnum(string value) => EnumUiHelper.ToEnum<ModePaiement>(value);
    
    public static ModePaiement DefaultSelected() => ModePaiement.OM;
}
