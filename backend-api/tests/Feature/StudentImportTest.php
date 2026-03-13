<?php

namespace Tests\Feature;

use App\Models\Course;
use App\Models\Student;
use App\Models\User;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Illuminate\Http\UploadedFile;
use Illuminate\Support\Facades\Storage;
use Tests\TestCase;

class StudentImportTest extends TestCase
{
    use RefreshDatabase;

    protected $admin;
    protected $course;

    protected function setUp(): void
    {
        parent::setUp();
        $this->admin = User::factory()->create(['role' => 'admin']);
        $this->course = Course::create(['name' => 'BS Computer Science']);
    }

    public function test_can_import_students_from_csv()
    {
        $csvContent = "student_number,name,course_id\n";
        $csvContent .= "2023-0001,John Doe,{$this->course->id}\n";
        $csvContent .= "2023-0002,Jane Smith,{$this->course->id}\n";

        $file = UploadedFile::fake()->createWithContent('students.csv', $csvContent);

        $response = $this->actingAs($this->admin)
            ->postJson('/api/v1/students/import', [
                'file' => $file
            ]);

        $response->assertStatus(200)
            ->assertJsonPath('data.success', 2)
            ->assertJsonPath('data.failed', 0);

        $this->assertDatabaseHas('students', ['student_number' => '2023-0001', 'name' => 'John Doe']);
        $this->assertDatabaseHas('students', ['student_number' => '2023-0002', 'name' => 'Jane Smith']);
    }

    public function test_import_handles_duplicates_gracefully()
    {
        Student::factory()->create(['student_number' => '2023-0001', 'course_id' => $this->course->id]);

        $csvContent = "student_number,name,course_id\n";
        $csvContent .= "2023-0001,Duplicate Student,{$this->course->id}\n";
        $csvContent .= "2023-0003,New Student,{$this->course->id}\n";

        $file = UploadedFile::fake()->createWithContent('students.csv', $csvContent);

        $response = $this->actingAs($this->admin)
            ->postJson('/api/v1/students/import', [
                'file' => $file
            ]);

        $response->assertStatus(200)
            ->assertJsonPath('data.success', 1)
            ->assertJsonPath('data.failed', 1);

        $this->assertCount(2, Student::all());
    }

    public function test_import_requires_csv_file()
    {
        $response = $this->actingAs($this->admin)
            ->postJson('/api/v1/students/import', [
                'file' => UploadedFile::fake()->create('students.pdf', 100)
            ]);

        $response->assertStatus(422)
            ->assertJsonValidationErrors(['file']);
    }
}
