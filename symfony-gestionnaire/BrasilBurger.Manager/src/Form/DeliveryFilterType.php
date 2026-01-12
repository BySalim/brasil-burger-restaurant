<?php

namespace App\Form;

use App\Enum\StatutLivraison;
use App\Repository\ZoneRepository;
use Symfony\Component\DependencyInjection\ParameterBag\ParameterBagInterface;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class DeliveryFilterType extends AbstractType
{
    public function __construct(
        private readonly ParameterBagInterface $params,
        private readonly ZoneRepository $zoneRepository
    ) {
    }

    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $perPageChoices = $this->params->get('app.pagination.per_page_choices');
        $defaultPerPage = $this->params->get('app.pagination.default_per_page');

        // Récupérer les zones actives pour le dropdown
        $zones = $this->zoneRepository->findBy(['estArchiver' => false], ['nom' => 'ASC']);
        $zoneChoices = [];
        foreach ($zones as $zone) {
            $zoneChoices[$zone->getNom()] = $zone->getId();
        }

        $builder
            ->add('search', TextType:: class, [
                'required' => false,
                'label' => false,
                'attr' => ['placeholder' => 'Rechercher par code...  ']
            ])
            ->add('date', DateType::class, [
                'required' => false,
                'label' => false,
                'widget' => 'single_text',
                'html5' => true,
            ])
            ->add('zone', ChoiceType:: class, [
                'required' => false,
                'label' => false,
                'placeholder' => 'Toutes les zones',
                'choices' => $zoneChoices,
            ])
            ->add('status', ChoiceType::class, [
                'required' => false,
                'label' => false,
                'placeholder' => 'Tous les statuts',
                'choices' => StatutLivraison::getChoices(),
                'choice_value' => 'value',
            ])
            ->add('per_page', ChoiceType::class, [
                'required' => false,
                'label' => false,
                'placeholder' => false,
                'choices' => $perPageChoices,
                'data' => $defaultPerPage,
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
