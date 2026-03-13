<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Http\Requests\Students\ImportStudentRequest;
use App\Http\Requests\Students\StoreStudentRequest;
use App\Http\Requests\Students\UpdateStudentRequest;
use App\Http\Resources\StudentResource;
use App\Services\StudentService;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class StudentController extends Controller
{
    protected $studentService;

    public function __construct(StudentService $studentService)
    {
        $this->studentService = $studentService;
    }

    public function index(Request $request): JsonResponse
    {
        $filters = $request->only(['student_number', 'name', 'course_id']);
        $students = $this->studentService->getAllStudents($filters);

        return $this->successResponse(
            StudentResource::collection($students),
            'Students retrieved successfully.'
        );
    }

    public function store(StoreStudentRequest $request): JsonResponse
    {
        $student = $this->studentService->createStudent($request->validated());

        return $this->successResponse(
            new StudentResource($student),
            'Student created successfully.',
            201
        );
    }

    public function show(string $id): JsonResponse
    {
        try {
            if (is_numeric($id)) {
                $student = $this->studentService->getStudent((int)$id);
            } else {
                $student = $this->studentService->getStudentByStudentNumber($id);
            }

            return $this->successResponse(
                new StudentResource($student),
                'Student details retrieved successfully.'
            );
        } catch (ModelNotFoundException $e) {
            return $this->errorResponse($e->getMessage(), 404);
        }
    }

    public function update(UpdateStudentRequest $request, int $id): JsonResponse
    {
        try {
            $student = $this->studentService->updateStudent($id, $request->validated());

            return $this->successResponse(
                new StudentResource($student),
                'Student updated successfully.'
            );
        } catch (ModelNotFoundException $e) {
            return $this->errorResponse($e->getMessage(), 404);
        }
    }

    public function destroy(int $id): JsonResponse
    {
        try {
            $this->studentService->deleteStudent($id);

            return $this->successResponse(
                null,
                'Student deleted successfully.'
            );
        } catch (ModelNotFoundException $e) {
            return $this->errorResponse($e->getMessage(), 404);
        }
    }

    public function import(ImportStudentRequest $request): JsonResponse
    {
        try {
            $results = $this->studentService->importStudents($request->file('file'));

            return $this->successResponse(
                $results,
                'Bulk import process completed.'
            );
        } catch (\Exception $e) {
            return $this->errorResponse($e->getMessage(), 500);
        }
    }
}
