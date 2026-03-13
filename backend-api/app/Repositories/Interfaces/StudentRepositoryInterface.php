<?php

namespace App\Repositories\Interfaces;

interface StudentRepositoryInterface
{
    public function getAll(array $filters = []);
    public function findByStudentNumber(string $studentNumber);
    public function findById(int $id);
    public function create(array $data);
    public function update(int $id, array $data);
    public function delete(int $id);
}