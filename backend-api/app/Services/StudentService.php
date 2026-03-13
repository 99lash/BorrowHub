<?php

namespace App\Services;

use App\Repositories\Interfaces\StudentRepositoryInterface;
use Illuminate\Database\Eloquent\ModelNotFoundException;

class StudentService
{
    protected $studentRepository;

    public function __construct(StudentRepositoryInterface $studentRepository)
    {
        $this->studentRepository = $studentRepository;
    }

    public function getStudentByStudentNumber(string $studentNumber)
    {
        $student = $this->studentRepository->findByStudentNumber($studentNumber);
        if (!$student) {
            throw new ModelNotFoundException('Student not found with student number: ' . $studentNumber);
        }

        return $student;
    }
}