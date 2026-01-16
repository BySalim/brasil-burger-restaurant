using BrasilBurger.Client.Infrastructure;

var builder = WebApplication.CreateBuilder(args);

// Charge appsettings.Local.json (non versionné) si présent
builder.Configuration.AddJsonFile("appsettings.Local.json", optional: true, reloadOnChange: true);

// DI
builder.Services.AddControllersWithViews();
builder.Services.AddInfrastructure(builder.Configuration);

// (Option) Cookie auth déjà prêt pour la suite
builder.Services.AddAuthentication("Cookies")
    .AddCookie("Cookies", opt =>
    {
        opt.LoginPath = "/Account/Login";
        opt.AccessDeniedPath = "/Account/Denied";
    });

var app = builder.Build();

// Render: écouter sur 0.0.0.0 + PORT (par défaut 10000 côté Render) :contentReference[oaicite:5]{index=5}
var port = Environment.GetEnvironmentVariable("PORT");
if (!string.IsNullOrWhiteSpace(port))
{
    app.Urls.Clear();
    app.Urls.Add($"http://0.0.0.0:{port}");
}

if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Home/Error");
    app.UseHsts();
}

app.UseHttpsRedirection();
app.UseStaticFiles();

app.UseRouting();

app.UseAuthentication();
app.UseAuthorization();

app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Home}/{action=Index}/{id?}");

app.Run();
