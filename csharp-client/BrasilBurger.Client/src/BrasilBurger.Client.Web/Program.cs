using BrasilBurger.Client.Infrastructure;
using BrasilBurger.Client.Web;

var builder = WebApplication.CreateBuilder(args);

// Fichier local non versionné: variables d'environnement
builder.Configuration.AddJsonFile("appsettings.Local.json", optional: true, reloadOnChange: true);

// MVC
builder.Services.AddControllersWithViews();

// Session pour TempData
builder.Services.AddSession(options =>
{
    options.IdleTimeout = TimeSpan.FromMinutes(20);
    options.Cookie.HttpOnly = true;
    options.Cookie.IsEssential = true;
    options.Cookie.Name = ".BrasilBurger.Session";
});

// Infrastructure (Neon + Cloudinary probes + options)
builder.Services.AddInfrastructure(builder.Configuration);

// Web
builder.Services.AddWeb();

// Cookie Authentication (connexion/inscription)
builder.Services.AddAuthentication("Cookies")
    .AddCookie("Cookies", opt =>
    {
        opt.LoginPath = "/Auth/Login";
        opt.AccessDeniedPath = "/Auth/Denied";
        opt.LogoutPath = "/Auth/Logout";
        
        // Configuration améliorée
        opt.ExpireTimeSpan = TimeSpan.FromDays(30);
        opt.SlidingExpiration = true;
        opt.Cookie.HttpOnly = true;
        opt.Cookie.SecurePolicy = CookieSecurePolicy.SameAsRequest;
        opt.Cookie.SameSite = SameSiteMode.Lax;
        opt.Cookie.Name = ".BrasilBurger.Auth";
    });

var app = builder.Build();

// Render: bind sur le port fourni par PORT si présent
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

// Session AVANT Authentication
app.UseSession();
app.UseAuthentication();
app.UseAuthorization();

app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Home}/{action=Index}/{id?}");

app.Run();
