<?php

namespace App\Form;

use Symfony\Component\DependencyInjection\ParameterBag\ParameterBagInterface;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class DelivererFilterType extends AbstractType
{
    public function __construct(
        private readonly ParameterBagInterface $params
    ) {
    }

    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $perPageChoices = $this->params->get('app.pagination.per_page_choices');
        $defaultPerPage = $this->params->get('app.pagination.default_per_page');

        $builder
            ->add('search', TextType::class, [
                'required' => false,
                'label' => false,
                'attr' => ['placeholder' => 'Rechercher...']
            ])
            ->add('disponibilite', ChoiceType::class, [
                'required' => false,
                'label' => false,
                'placeholder' => 'Disponibilité',
                'choices' => [
                    'Disponible' => true,
                    'En livraison' => false,
                ],
            ])
            ->add('per_page', ChoiceType::class, [
                'required' => false,
                'label' => false,
                'placeholder' => false,
                'choices' => $perPageChoices,
                'data' => $defaultPerPage,
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
