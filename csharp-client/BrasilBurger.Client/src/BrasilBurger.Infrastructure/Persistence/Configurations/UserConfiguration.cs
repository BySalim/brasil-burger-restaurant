using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using BrasilBurger.Domain.Entities;

namespace BrasilBurger.Infrastructure.Persistence.Configurations;

public class UserConfiguration : IEntityTypeConfiguration<User>
{
    public void Configure(EntityTypeBuilder<User> builder)
    {
        builder.HasKey(u => u.Id);
        builder.Property(u => u.Nom).IsRequired().HasMaxLength(100);
        builder.Property(u => u.Prenom).IsRequired().HasMaxLength(100);
        builder.Property(u => u.Login).IsRequired().HasMaxLength(100).IsUnicode();
        builder.Property(u => u.MotDePasse).IsRequired().HasMaxLength(255);
        builder.Property(u => u.Role).IsRequired();
        builder.Property(u => u.Telephone).HasMaxLength(20);
        builder.HasIndex(u => u.Login).IsUnique();
        builder.HasIndex(u => u.Telephone).IsUnique();
    }
}