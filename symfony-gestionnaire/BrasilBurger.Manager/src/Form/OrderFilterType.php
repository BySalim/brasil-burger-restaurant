<?php

namespace App\Form;

use App\Enum\EtatCommande;
use App\Enum\CategoriePanier;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\EnumType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class OrderFilterType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('search', TextType::class, [
                'required' => false,
            ])
            ->add('etat', EnumType::class, [
                'class' => EtatCommande::class,
                'choice_label' => fn (EtatCommande $choice) => $choice->name,
                'required' => false,
                'placeholder' => 'Tous les statuts',
            ])
            ->add('type', EnumType::class, [
                'class' => CategoriePanier::class,
                'choice_label' => fn (CategoriePanier $choice) => $choice->getLabel(),
                'required' => false,
                'placeholder' => 'Tous les types',
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'method' => 'GET',
            'csrf_protection' => false,
        ]);
    }
}
