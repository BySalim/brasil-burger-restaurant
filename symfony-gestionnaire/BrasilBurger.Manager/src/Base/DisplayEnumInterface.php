<?php

namespace App\Base;

interface DisplayEnumInterface
{
    public function getLabel(): string;
    public function getColor(): string;
    public function getIcon(): string;
    public function getIconBg(): string;
}
