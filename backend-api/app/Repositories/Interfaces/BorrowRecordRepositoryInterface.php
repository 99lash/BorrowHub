<?php

namespace App\Repositories\Interfaces;

interface BorrowRecordRepositoryInterface
{
    public function getActiveRecords(array $filters = []);
    public function findById(int $id);
    public function updateStatus(int $id, string $status, $returnedAt = null);
}
