<?php

namespace App\Form;

use App\Enum\CategoriePanier;
use App\Enum\EtatCommande;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\IntegerType;
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
                'label' => false,
                'attr' => ['placeholder' => 'Rechercher par code... ']
            ])
            ->add('date', DateType::class, [
                'required' => false,
                'label' => false,
                'widget' => 'single_text',
                'html5' => true,
            ])
            ->add('type', ChoiceType::class, [
                'required' => false,
                'label' => false,
                'placeholder' => 'Tous les types',
                'choices' => CategoriePanier::getChoices(),
                'choice_value' => 'value',
            ])
            ->add('status', ChoiceType::class, [
                'required' => false,
                'label' => false,
                'placeholder' => 'Tous les statuts',
                'choices' => EtatCommande::getChoices(),
                'choice_value' => 'value',
            ])
            ->add('per_page', ChoiceType::class, [
                'required' => false,
                'label' => false,
                'placeholder' => false,
                'choices' => [
                    '4' => 4,
                    '8' => 8,
                    '15' => 15,
                    '20' => 20,
                ],
                'data' => 4,
                'attr' => ['class' => 'w-full px-3 py-2.5'],
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'method' => 'GET',
            'csrf_protection' => false,
        ]);
    }

    public function getBlockPrefix(): string
    {
        return '';
    }
}
