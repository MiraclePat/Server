package com.miraclepat.global.exception;

public class ErrorMessage {
    //Auth
    public static final String NOT_EXIST_MEMBER = "가입 정보를 찾을 수 없습니다.";
    public static final String EXIST_MEMBER = "이미 가입된 회원입니다.";

    // Firebase
    public static final String FAIL_TOKEN_GENERATION = "토큰 생성 실패";
    public static final String FAIL_USER_CREATION = "유저 생성 실패. 이미 등록된 사용자";
    public static final String FAIL_USER_DELETION = "유저 삭제 실패";
    public static final String FAIL_GET_UID = "Uid를 찾을 수 없습니다.";

    // File
    public static final String NO_FILE_NAME = "파일 원본 이름은 필수입니다.";

    // Storage
    public static final String FAIL_IMAGE_UPLOAD = "이미지 업로드 실패";

    // Common
    public static final String NOT_EXIST_MEMBER_INFO = "회원 정보를 찾을 수 없습니다.";
    public static final String NOT_EXIST_PAT = "존재하지 않는 팟입니다.";
    public static final String NOT_JOIN_PAT = "참여한 팟이 아닙니다.";

    // Member
    public static final String EXIST_NICKNAME = "이미 존재하는 닉네임입니다.";
    public static final String EMPTY_FILE = "이미지가 비어있습니다.";

    // Pat
    public static final String START_DATE_MINIMUM_TWO_DAYS_LATER = "시작일은 오늘로부터 최소 이틀 이후여야 합니다.";
    public static final String ONLY_WRITER_CAN_MODIFY = "작성자만 수정 가능합니다.";
    public static final String CANNOT_ONE_DAY_BEFORE_START = "시작일 하루 전부터는 불가합니다.";
    public static final String MAX_PERSON = "최대 인원에 도달해 참여 불가합니다.";
    public static final String ALREADY_JOIN = "이미 참여중인 팟입니다.";
    public static final String WRITER_UNABLE_TO_LEAVE = "작성자는 참여 취소가 불가합니다.";
    public static final String END_DATE_AFTER_START = "종료일은 시작일 보다 이후여야 합니다.";
    public static final String END_TIME_AFTER_START = "종료시간은 시작 시간 보다 이후여야 합니다.";
    public static final String SMALLER_THAN_NOW_PERSON = "최대 인원이 현재 참여 인원보다 작습니다.";
    public static final String JOIN_ONLY_SCHEDULED = "팟 시작 전에만 참여 가능합니다.";

    // Proof
    public static final String NOT_IN_PROGRESS = "인증 진행중이 아닙니다.";
    public static final String NOT_PROOF_DAY = "인증일이 아닙니다.";
    public static final String NOT_PROOF_TIME = "인증 시간이 아닙니다.";
    public static final String EXIST_TODAY_PROOF = "오늘의 인증 내역 존재합니다.";
}
