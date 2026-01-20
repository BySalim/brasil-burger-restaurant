using BrasilBurger.Client.Infrastructure;

var builder = WebApplication.CreateBuilder(args);

// Fichier local non versionné (ignore via .gitignore)
builder.Configuration.AddJsonFile("appsettings.Local.json", optional: true, reloadOnChange: true);

// MVC
builder.Services.AddControllersWithViews();

// Infra (Neon + Cloudinary probes + options)
builder.Services.AddInfrastructure(builder.Configuration);

// (Optionnel) Cookie auth pour la suite (connexion/inscription)
builder.Services.AddAuthentication("Cookies")
    .AddCookie("Cookies", opt =>
    {
        opt.LoginPath = "/Account/Login";
        opt.AccessDeniedPath = "/Account/Denied";
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

app.UseAuthentication();
app.UseAuthorization();

app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Home}/{action=Index}/{id?}");

app.Run();
