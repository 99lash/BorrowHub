<?php

namespace Tests\Unit;

use Tests\TestCase;
use App\Models\User;
use App\Models\Course;
use App\Models\Student;
use App\Models\Category;
use App\Models\Item;
use App\Models\BorrowRecord;
use Illuminate\Foundation\Testing\RefreshDatabase;
// use PHPUnit\Framework\TestCase;

class ModelRelationshipTest extends TestCase
{
    use RefreshDatabase;

    public function test_course_has_many_students()
    {
        $course = Course::factory()->has(Student::factory()->count(3))->create();
        $this->assertCount(3, $course->students);
        $this->assertInstanceOf(Student::class, $course->students->first());
    }
    
    public function test_student_belongs_to_course()
    {
        $student = Student::factory()->create();
        $this->assertInstanceOf(Course::class, $student->course);
    }

    public function test_category_has_many_items()
    {
        $category = Category::factory()->has(Item::factory()->count(2))->create();
        $this->assertCount(2, $category->items);
    }

    public function test_borrow_record_belongs_to_student_and_staff()
    {
        $borrowRecord = BorrowRecord::factory()->create();
        $this->assertInstanceOf(Student::class, $borrowRecord->student);
        $this->assertInstanceOf(User::class, $borrowRecord->staff);
    }

    public function test_borrow_record_has_many_items_via_pivot()
    {
        $borrowRecord = BorrowRecord::factory()->create();
        $items = Item::factory()->count(2)->create();

        $borrowRecord->items()->attach($items, ['quantity' => 1]);

        $this->assertCount(2, $borrowRecord->items);
        $this->assertEquals(1, $borrowRecord->items()->first()->pivot->quantity);
    }
}
