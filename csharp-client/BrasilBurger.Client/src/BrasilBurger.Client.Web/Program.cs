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
        opt.AccessDeniedPath = "/Error/403";
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

// ============================================
// CONFIGURATION DE LA GESTION DES ERREURS
// ============================================
if (!app.Environment.IsDevelopment())
{
    // Gestion des exceptions non gérées (500)
    app.UseExceptionHandler("/Error/500");
    
    // Gestion des codes de statut HTTP (404, 403, etc.)
    app.UseStatusCodePagesWithReExecute("/Error/{0}");
    
    app.UseHsts();
}
else
{
    // En développement, afficher la page d'exception détaillée
    app.UseDeveloperExceptionPage();
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