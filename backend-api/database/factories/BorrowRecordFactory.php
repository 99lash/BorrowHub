<?php

namespace Database\Factories;

use App\Models\Student;
use App\Models\User;
use Illuminate\Database\Eloquent\Factories\Factory;

/**
 * @extends \Illuminate\Database\Eloquent\Factories\Factory<\App\Models\BorrowRecord>
 */
class BorrowRecordFactory extends Factory
{
    /**
     * Define the model's default state.
     *
     * @return array<string, mixed>
     */
    public function definition(): array
    {
        return [
            'student_id' => Student::factory(),
            'staff_id' => User::factory(),
            'collateral' => fake()->randomElement(['Student ID', 'Valid ID']),
            'borrowed_at' => now(),
            'due_at' => now()->addDays(3),
            'status' => 'borrowed',
        ];
    }
}
