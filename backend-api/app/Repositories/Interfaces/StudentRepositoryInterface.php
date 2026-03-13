<?php

namespace App\Repositories\Interfaces;

interface StudentRepositoryInterface
{
    public function findByStudentNumber(string $studentNumber);
}