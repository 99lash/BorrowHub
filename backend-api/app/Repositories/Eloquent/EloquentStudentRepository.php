<?php

namespace App\Repositories\Eloquent;

use App\Models\Student;
use App\Repositories\Interfaces\StudentRepositoryInterface;

class EloquentStudentRepository implements StudentRepositoryInterface
{
    public function findByStudentNumber(string $studentNumber)
    {
        return Student::with('course')
                ->where('student_number', $studentNumber)
                ->first();
    }
}