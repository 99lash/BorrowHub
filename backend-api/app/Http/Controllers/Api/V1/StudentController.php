<?php
namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Http\Resources\StudentResource;
use App\Services\StudentService;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Illuminate\Http\JsonResponse;

class StudentController extends Controller
{
    protected $studentService;

    public function __construct(StudentService $studentService)
    {
        $this->studentService = $studentService;
    }

    public function show(string $studentNumber): JsonResponse
    {
        try {
            $student = $this->studentService->getStudentByStudentNumber($studentNumber);

            return $this->successResponse(
                new StudentResource($student),
                'Student details retrieved successfully.'
            );
        } catch (ModelNotFoundException $e) {
            return $this->errorResponse($e->getMessage(), 404);
        }
    }
}
